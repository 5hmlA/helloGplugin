package jzy.spark.tellu.wizard

import jzy.spark.tellu.data.Emotion
import jzy.spark.tellu.room.table.EmotionData
import jzy.spark.tellu.wizard.illsuion.*

interface IllusionWizardOrganization {

    /**
     * 管理所有幻术巫师
     * @param emotion 需要转换的数据
     * @return  转换后的数据
     */
    fun magicTransform(emotionTable: EmotionData): Emotion {
        illusionWizards.forEach {
            if (it.magicTransform(emotionTable)) {
                return@forEach
            }
        }
        return Emotion(emotionTable.title, emotionTable.tips, emotionTable.extra)
    }

    /**
     * 加入组织的巫师们
     * 配置好所有幻术巫师 转换情感数据
     */
    val illusionWizards: List<IllusionWizard>

}

/**
 * 幻术巫师
 */
interface IllusionWizard {

    /**
     * 修改emotion内容
     *
     * @param formartStr 需要转换的类型
     * @return  true 交给下一个处理 ()
     */
    fun magicTransform(emotionTable: EmotionData): Boolean

    /**
     * 支持转换的类型id
     * @return
     */
    val formartId: List<Int>
}

class IllusionWizardOrganizationImpl : IllusionWizardOrganization {
    /**
     * 只需要 配置好所有幻术巫师 转换情感数据
     *      1- 国内版本要配置所有转换逻辑
     *      2- 海外版本只需要目标值和目标完成率
     *      3- 游客模式 只显示原内容的数据，只需要处理 当需要转换的时候就抛异常，让魔法阵重新发动，跳过需要转换的数据
     */
    override val illusionWizards: List<IllusionWizard> by lazy {
        mutableListOf<IllusionWizard>(Illsuion1Wizard(), Illsuion2Wizard(), Illsuion3Wizard(), Illsuion4Wizard())
    }
}

class IllusionWizardOrganizationOverseaImpl : IllusionWizardOrganization {
    /**
     * 只需要 配置好所有幻术巫师 转换情感数据
     *      1- 国内版本要配置所有转换逻辑
     *      2- 海外版本只需要目标值和目标完成率
     *      3- 游客模式 只显示原内容的数据，只需要处理 当需要转换的时候就抛异常，让魔法阵重新发动，跳过需要转换的数据
     */
    override val illusionWizards: List<IllusionWizard> by lazy {
        mutableListOf<IllusionWizard>(IllsuionStrWizard(), Illsuion1Wizard(), Illsuion2Wizard(), Illsuion3Wizard(), Illsuion4Wizard())
    }
}
