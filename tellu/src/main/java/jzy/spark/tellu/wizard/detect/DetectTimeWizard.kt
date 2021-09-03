package jzy.spark.tellu.wizard.detect

import jzy.spark.tellu.data.EmotionSet
import jzy.spark.tellu.wizard.DetectWizard
import io.reactivex.Observable
import java.util.*

class DetectTimeWizard : DetectWizard {
    //    清晨问候 处在5:00-8:00时，合适的问候语  500
    //    上午问候 处在8:00-11:40， 合适的问候语  501
    //    中午问候 处在11:40-14:00，合适的问候语  502
    //    下午问候 处在14:00-17:40，合适的问候语  503
    //    傍晚问候 处在17:40-19:30，合适的问候语  504
    //    夜间问候 处在19:30-22:00，合适的问候语  505
    //    深夜问候 处在22:00-1:00， 合适的问候语  506
    //    凌晨问候 处在1:00-5:00，  合适的问候语  507
    //    清晨问候 处在5:00-8:00时，合适的问候语  500
    //    上午问候 处在8:00-11:40， 合适的问候语  501
    //    中午问候 处在11:40-14:00，合适的问候语  502
    //    下午问候 处在14:00-17:40，合适的问候语  503
    //    傍晚问候 处在17:40-19:30，合适的问候语  504
    //    夜间问候 处在19:30-22:00，合适的问候语  505
    //    深夜问候 处在22:00-1:00， 合适的问候语  506
    //    凌晨问候 处在1:00-5:00，  合适的问候语  507

    override fun magicDetect(): Observable<List<EmotionSet>> {
        return Observable.create{
            val instance = Calendar.getInstance()
            val hour = instance[Calendar.HOUR_OF_DAY]
            val minute = instance[Calendar.MINUTE]
            val ctime = hour * 100 + minute
            val mutableListOf = mutableListOf<Period>(
                Period(506, ctime, 0, 100),
                Period(507, ctime, 100, 500),
                Period(500, ctime, 500, 800),
                Period(501, ctime, 800, 1140),
                Period(502, ctime, 1140, 1400),
                Period(503, ctime, 1400, 1740),
                Period(504, ctime, 1740, 1930),
                Period(505, ctime, 1930, 2200),
                Period(506, ctime, 2200, 2359)
            )
            val emotionSets = mutableListOf.map { it.detect() }
            it.onNext(emotionSets)
            it.onComplete()
        }
    }

    //时间检测分为 多个时间段 各自有id
    override fun typeId() = 0
}

class Period(val typeId: Int, val ctime: Int, val stime: Int, val etime: Int) {
    fun detect(): EmotionSet {
        //判断当前时间是否在时间段之内
        //            Calendar instance = Calendar.getInstance();
        //            int hour = instance.get(Calendar.HOUR_OF_DAY);
        //            int minute = instance.get(Calendar.MINUTE);
        //            int cNtime = hour * 100 + minute;

        return if (ctime in stime until etime) {
            EmotionSet(typeId, null, false)
        } else {
            EmotionSet(typeId, null, true)
        }
    }
}