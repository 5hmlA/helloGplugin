package jzy.spark.tellu.wizard

import android.annotation.SuppressLint
import jzy.spark.tellu.Utills
import jzy.spark.tellu.data.EmotionSet;
import jzy.spark.tellu.room.Repository2
import jzy.spark.tellu.wizard.detect.DetectHolidayWizard
import jzy.spark.tellu.wizard.detect.DetectTimeWizard

import io.reactivex.Observable;
import java.util.*

interface DetectWizardOrganization {

    /**
     * 管理所有检测类型
     * 更新动态情感的曝光到数据库 显示时间设置为今天
     * @return 检测结果
     */
    @SuppressLint("CheckResult")
    fun magicDetect(): Observable<List<EmotionSet>> {
        //计算所有情感
       return Observable.zip(detectWizards.map { it.magicDetect() }) { array ->
           Utills.elog("magicDetect emotionSets ${array.size}  ${array.contentToString()}")
           return@zip array.map { it as List<EmotionSet> }.flatten().toList().also { Utills.elog(it.toString()) }.map {
               //更新到数据库
               Repository2.instance.updateByEmotionSet(it)
               return@map it
            }
        }
    }

    /**
     * 加入组织的巫师们
     * 配置好所有监测巫师 监测动态情感数据
     */
    val detectWizards: List<DetectWizard>

}

/**
 * 监测巫师
 */
interface DetectWizard {
    /**
     * 根据数据类型 做对应检测
     * 有监测出需要曝光，有监测出不需要曝光
     * @return 检测结果 之后用来更新数据库
     */
    fun magicDetect(): Observable<List<EmotionSet>>

    /**
     * 负责检测哪类数据
     * @return
     */
    fun typeId(): Int
}

class DetectWizardOrganizationImpl : DetectWizardOrganization {

    /**
     * 只需要 配置好所有监测巫师 监测动态情感数据
     *      1- 国内版本要检测所有逻辑
     *      2- 海外版本 只需要检测目标完成率
     *      3- 游客模式不需要任何检测
     */
    override val detectWizards: List<DetectWizard> by lazy {
        //配置好所有检测逻辑
        mutableListOf(DetectTimeWizard(), DetectHolidayWizard())
    }
}
