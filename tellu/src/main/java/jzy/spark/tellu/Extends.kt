package jzy.spark.tellu

import jzy.spark.tellu.Extends.Companion.sDateFormat
import jzy.spark.tellu.room.table.EmotionData
import java.text.SimpleDateFormat
import java.util.*

class Extends {
    companion object{
        val sDateFormat = SimpleDateFormat("MM月dd日E")
    }



}

fun EmotionData.isDynamic() = typeId >= 500

    //默认有实现
//    operator fun <E> MutableList<E>.plus(beadd: MutableList<E>): MutableList<E> {
//        addAll(beadd)
//        return this
//    }

infix fun <E> MutableList<E>.pluss(beadd: MutableList<E>): MutableList<E> {
    addAll(beadd)
    return this
}



fun Throwable.log():Throwable {
    Utills.elog(this)
    return this
}
fun Date.toShowTime(): String = sDateFormat.format(this)

//inline fun  InputStreamReader.toData(fileName:String){
//    val jsonReader = JsonReader(InputStreamReader(Utills.context.assets.open("iniemodata.json")))
//    try {
//        val toList = gson.fromJson<List<EmotionTable>>(jsonReader, object : TypeToken<List<EmotionTable>>() {}.type)
//        it.onNext(toList)
//        it.onComplete()
//    } catch (e: Exception) {
//    }
//}