package jzy.spark.tellu.wizard.illsuion

import jzy.spark.tellu.room.table.EmotionData
import jzy.spark.tellu.toShowTime
import jzy.spark.tellu.wizard.IllusionWizard
import java.security.SecureRandom
import java.util.*

class Illsuion2Wizard: IllusionWizard {
    override fun magicTransform(emotionData: EmotionData): Boolean {
        if (formartId.contains(emotionData.formatId)) {
            //具体转换
            emotionData.tips = String.format(emotionData.tips, Date().toShowTime(), SecureRandom().nextInt(100) + 1)
            return true
        }
        return false
    }

    override val formartId: List<Int> by lazy {
        mutableListOf(2)
    }
}