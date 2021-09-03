package jzy.spark.tellu.wizard.illsuion

import jzy.spark.tellu.room.table.EmotionData
import jzy.spark.tellu.wizard.IllusionWizard
import java.lang.RuntimeException

class IllsuionNoWizard : IllusionWizard {
    override fun magicTransform(emotionData: EmotionData): Boolean {
        if (emotionData.formatId > 0) {
            //遇到有要装换的数据 就跑异常 下游捕获 重试 就会取另一个情感数据了
            throw RuntimeException("tourist mode no data to illsuion")
        }
        return true
    }

    override val formartId: List<Int> by lazy {
        mutableListOf(1)
    }
}