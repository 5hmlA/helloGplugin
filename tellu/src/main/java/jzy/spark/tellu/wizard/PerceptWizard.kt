package jzy.spark.tellu.wizard

import jzy.spark.tellu.room.Repository2
import jzy.spark.tellu.room.table.EmotionData

import io.reactivex.Observable;

interface PerceptWizard {

    /**
     * 根据规则查找要显示的数据
     * <li> 1，权重最大</li>
     * <li> 2，曝光次数不为0</li>
     * <li> 3，把找到的数据曝光-1</li>
     * <li> 4，设置显示时间为今天</li>
     * 以上如果找不到需要显示的情感数据，就随机选择曝光次数不限(-1)的情感数据
     *
     * @return 返回要显示的数据
     */
    fun magicSearch(): Observable<EmotionData>
}

class PerceptWizardImpl : PerceptWizard {
    override fun magicSearch(): Observable<EmotionData> = Observable.just(Repository2.instance.perceptMagic())
}
