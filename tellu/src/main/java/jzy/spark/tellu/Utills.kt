package jzy.spark.tellu

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import jzy.spark.tellu.room.table.EmotionData
import java.security.SecureRandom
import java.util.*

class Utills private constructor() {
    companion object {
        private var checkTime = 0L
        private var dayDateNum = 0

        @SuppressLint("StaticFieldLeak")
        internal lateinit var context: Context

        @JvmStatic
        fun keepContext(context: Context) {
            Companion.context = context
        }

        @JvmStatic
        fun getContext() = context

        fun todayDateNum() =
            if (DateUtils.isToday(checkTime)) {
                dayDateNum
            } else {
                val calendar = Calendar.getInstance()
                checkTime = calendar.timeInMillis
                (calendar[Calendar.MONTH] * 100 + calendar[Calendar.DAY_OF_MONTH]).also {
                    dayDateNum = it
                }
            }

        fun elog(vararg msgs: Any) {
            Log.e("Emotion", TextUtils.join(",", msgs))
        }

        fun elog(error: Throwable) {
            Log.e("Emotion", "error ", error)
        }

        fun elog(msg: String) {
            Log.d("Emotion", msg)
        }

        /**
         * 从同一类数据中选中一条或几条来允许曝光
         */
        fun choseWhoTobeShow(emotionTables: List<EmotionData>): List<EmotionData> {
            if (emotionTables.isNotEmpty()) {
                val exposureFix = emotionTables[0].exposureFix
                if (exposureFix <= 1) {
                    return mutableListOf(emotionTables.random().also {
                        it.exposure = exposureFix
                    })
                } else {
                    //随机选一个或者 共享曝光
                    mutableListOf(emotionTables.random().also {
                        it.exposure = exposureFix
                    })
                }
            }
            return emotionTables
        }

        private fun randomShareExposure(emotionTables: List<EmotionData>, exposureFix: Int) {
            var exposureNum = exposureFix
            if (emotionTables.size == 1) {
                emotionTables[0].exposure = exposureNum
                emotionTables[0].showDate = todayDateNum()
            } else {
                val iterator = emotionTables.iterator()
                var last: EmotionData? = null
                val secureRandom = SecureRandom()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    last = next
                    if (secureRandom.nextBoolean()) {
                        if (exposureNum > 0) {
                            if (exposureNum > 1) {
                                val anInt = secureRandom.nextInt(exposureNum) + 1
                                exposureNum -= anInt
                                next.exposure = anInt
                                next.showDate = todayDateNum()
                            } else {
                                exposureNum = 0
                                next.exposure = 1
                                next.showDate = todayDateNum()
                            }
                        }
                    }
                }
                if (last != null && exposureNum > 0) {
                    last.exposure = exposureNum
                    last.showDate = todayDateNum()
                }
            }
        }

    }


}