package jzy.spark.tellu.wizard.detect

import android.text.format.DateUtils
import jzy.spark.tellu.Utills
import jzy.spark.tellu.convert.LunarCalendar
import jzy.spark.tellu.data.EmotionSet
import jzy.spark.tellu.wizard.DetectWizard
import io.reactivex.Observable
import java.util.*

class DetectHolidayWizard : DetectWizard {

    var detectTime = 0L

    override fun magicDetect(): Observable<List<EmotionSet>> {
       return Observable.create<List<EmotionSet>> {
           if (DateUtils.isToday(detectTime)) {
               it.onNext(Collections.emptyList())
               it.onComplete()
               return@create
           }
            detectTime = System.currentTimeMillis()
            val holiday = LunarCalendar.getHoliday()
            if (holiday.isNotEmpty()) {
                Utills.elog("DetectHolidayWizard >> $holiday")
                it.onNext(mutableListOf(EmotionSet(typeName = holiday)))
                it.onComplete()
                return@create
            }
           it.onNext(Collections.emptyList())
           it.onComplete()
        }
    }

    //节假日 数据用typeName匹配 不需要typeId
    override fun typeId() = 0
}