package jzy.spark.tellu.wizard

import jzy.spark.tellu.room.Repository2
import io.reactivex.Observable

interface ResetWizard {

    /**
     * 根据需要重置 情感数据
     * <b> 显示时间不为空且不是今天的全部需要重置
     *  <li>1，动态情感数据：（包括推送的）显示时间如果不是今天 就移除曝光 处理昨天的推送还没显示的情况</li>
     *  <li>2，静态情感数据：曝光为初始值</li>
     *  </b>
     * @return
     */
    fun magicReset(): Observable<Any>
}

class ResetWizardImpl : ResetWizard {
    override fun magicReset(): Observable<Any> = Observable.create {
        //1，动态情感数据(包括推送)：显示时间如果不是今天 就移除曝光 移除显示时间
        //2，静态情感数据：显示时间如果不是今天 曝光为初始值 移除显示时间
        Repository2.instance.resetMagic()
        it.onNext("")
    }

}
