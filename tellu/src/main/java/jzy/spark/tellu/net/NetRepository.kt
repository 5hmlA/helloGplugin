package jzy.spark.tellu.net

import android.text.format.DateUtils
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import jzy.spark.tellu.Utills
import jzy.spark.tellu.isDynamic
import jzy.spark.tellu.room.emotionDao
import jzy.spark.tellu.room.table.EmotionData
import io.reactivex.Observable
import java.io.InputStreamReader
import java.util.*


class NetRepository : NetApi {

    companion object {
        var checkTime = 0L
        fun needUpdate(): Boolean = DateUtils.isToday(checkTime).also {
            checkTime = System.currentTimeMillis()
        }
    }

    override fun getEmotionalList(version: Long): Observable<List<EmotionData>> {
        if (needUpdate()) {
            return Observable.just(Collections.emptyList())
        }
        return Observable.create<List<EmotionData>> {

            if (emotionDao.queryCount() > 0) {
                it.onNext(Collections.emptyList())
                it.onComplete()
                return@create
            }
//            val gson = Gson()
//            val reader = JsonReader(FileReader(filename))
//            val data: Review = gson.fromJson(reader, Review::class.java)
//            data.toScreen() // prints to screen some values

//            val dataStr = Utills.context.assets.open("iniemodata.json").bufferedReader().use { it.readText() }
//
//            Json.decodeFromString<ddd>(ddd,dataStr)

            val gson = GsonBuilder().create()
//            val toList = gson.fromJson(dataStr, Array<EmotionTable>::class.java).toList()

            val jsonReader = JsonReader(InputStreamReader(Utills.context.assets.open("iniemodata.json")))
            try {
                val toList = gson.fromJson<List<EmotionData>>(jsonReader, object : TypeToken<List<EmotionData>>() {}.type)
                toList.filter { it.isDynamic() }.onEach { it.exposure = 0 }//动态情感数据默认没曝光
                //静态情感数据 清除显示时间，分配重置曝光
                toList.asSequence()
                    .filter { !it.isDynamic() }
                    .onEach {
                        it.exposure = 0
                        it.showDate = 0
                    }.groupBy { it.typeId }
                    .onEach { entry ->
                        Utills.choseWhoTobeShow(entry.value)
                    }

                it.onNext(toList)
                it.onComplete()

            } catch (e: Exception) {
            }
        }
    }

}