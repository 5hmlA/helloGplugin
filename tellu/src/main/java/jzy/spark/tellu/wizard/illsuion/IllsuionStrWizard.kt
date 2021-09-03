package jzy.spark.tellu.wizard.illsuion

import jzy.spark.tellu.room.table.EmotionData
import jzy.spark.tellu.wizard.IllusionWizard

class IllsuionStrWizard : IllusionWizard {
    override fun magicTransform(emotionData: EmotionData): Boolean {
        //所有数据都要转换 字符串资源名字转为字符串具体值
//            emotionTable.tips = ""
//            emotionTable.tips = ""
        return false
    }

    override val formartId: List<Int> by lazy {
        mutableListOf(1)
    }
}