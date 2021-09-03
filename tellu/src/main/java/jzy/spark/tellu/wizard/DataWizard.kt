package jzy.spark.tellu.wizard;

import jzy.spark.tellu.net.NetApi
import jzy.spark.tellu.net.NetRepository
import jzy.spark.tellu.room.Repository2
import io.reactivex.Observable

interface DataWizard {

    /**
     * 初始化或者更新数据
     * @return
     */
    fun magicSummonData(): Observable<Any>
}

class DataWizardImpl(var netApi: NetApi = NetRepository()): DataWizard {
    override fun magicSummonData(): Observable<Any> {
        return netApi.getEmotionalList(0).doOnNext {
            it.takeIf { it.isNotEmpty() }?.let { data ->
                Repository2.instance.insertAll(data)
            }
        }.map { it }

    }
}
