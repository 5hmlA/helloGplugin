package jzy.spark.tellu.rule

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jzy.spark.tellu.Mock
import jzy.spark.tellu.Utills
import jzy.spark.tellu.data.Emotion
import jzy.spark.tellu.log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

interface MagicCircle {

    /**
     * UI层 在首页重新可见的时候调用
     * 念咒 施展魔法阵 执行情感数据更新逻辑
     * 有防抖处理，太频繁念咒会无效 此方法有冷却时间
     */
    fun spell()

    /**
     * UI层用来监听 情感数据的变化
     */
    fun obsEmotionChange(): LiveData<Emotion>

    fun release()
}

@SuppressLint("CheckResult")
class MagicCircleImpl(val magicAcademy: MagicAcademy) : MagicCircle, ViewModel() {

    val emotionLivedata = MutableLiveData<Emotion>()

    /**
     * 模仿rxandroid对点击事件防抖的处理
     */
    val mock = Mock()

    private var subscribe: Disposable

    init {
        subscribe = mock.listen(10).flatMap {
            //10秒防抖处理，十秒内忽略
            unleaseMagic()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                //通过livedata通知UI更新界面情感数据
                emotionLivedata.postValue(it)
            }) {
                it.log()
            }
    }

    /**
     * <p>施展魔法 更新情感数据</p>
     *
     * 情感数据更新规则
     * 1，初始化情感数据/尝试更新情感数据
     * 2，动态计算情感数据是否要显示
     * 3，尝试重置情感数据(一天执行一次即可)
     * 4，查找需要显示的情感
     * 5，显示的内容转换
     */
    @SuppressLint("CheckResult")
    private fun unleaseMagic(): Observable<Emotion> {

        //step 1
        Utills.elog("step 1 data init $this")
        return magicAcademy.dataWizard.magicSummonData().flatMap {
            //step 2
            Utills.elog("step 2 magicDetect $this")
            magicAcademy.detectWizard.magicDetect().flatMap {
                //step 3
                Utills.elog("step 3 magicReset $this")
                magicAcademy.resetWizard.magicReset().flatMap {
                    //step 4
                    Utills.elog("step 4 magicSearch $this")
                    magicAcademy.perceptWizard.magicSearch().map {
                        //step 5
                        Utills.elog("step 4 magicTransform $this")
                        magicAcademy.illusionWizard.magicTransform(it)
                    }
                }
            }
        }.retry(6){
            it.log()
            true
        }
    }

    /**
     * UI层 在首页重新可见的时候调用
     * 念咒 施展魔法阵 执行情感数据更新逻辑
     * 有防抖处理，太频繁念咒会无效 此方法有冷却时间
     */
    override fun spell() {
        mock.execute()
    }

    /**
     * UI层用来监听 情感数据的变化
     */
    override fun obsEmotionChange() = emotionLivedata

    override fun release() {
        subscribe.takeIf { it.isDisposed }?.dispose()
    }

}
