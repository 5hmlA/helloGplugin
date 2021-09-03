package jzy.spark.tellu.net

import jzy.spark.tellu.room.table.EmotionData
import io.reactivex.Observable

interface NetApi {
    fun getEmotionalList(version: Long): Observable<List<EmotionData>>
}