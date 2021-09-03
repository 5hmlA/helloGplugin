package jzy.spark.tellu.room

import jzy.spark.tellu.Utills
import jzy.spark.tellu.data.EmotionSet
import jzy.spark.tellu.isDynamic
import jzy.spark.tellu.room.dao.EmotionDao
import jzy.spark.tellu.room.table.EmotionData

val emotionDao: EmotionDao = AppDataBase.getInstance(Utills.context).emotionDao()

class Repository2 private constructor() {
    companion object {
        val instance: Repository2 by lazy {
            Repository2()
        }
    }

    fun insertAll(datas: List<EmotionData>) {
        emotionDao.insert(datas)
    }


    /**
     * 更新动态情感数据
     * 1，移除曝光
     * 2，设置曝光
     */
    fun updateByEmotionSet(emotionSet: EmotionSet) {
        //1 设置曝光
        //2 移除曝光

        val queryData = emotionDao.queryData(emotionSet.typeId, emotionSet.typeName)
        if (queryData.isNotEmpty()) {
            var updateData: List<EmotionData>? = null
            if (emotionSet.rmexposure) {
                //移除曝光
                //剩余曝光大于0 的数据移除 比如上午情感要曝光但是没来得及曝光到下午了 就需要吧上午的移除
                updateData = queryData
                    .filter { it.showDate > 0 }
                    .onEach {
                        it.exposure = 0
                        it.showDate = 0
                    }
                Utills.elog("移除曝光 ${emotionSet.typeId}, ${emotionSet.typeName ?: ""}，${updateData.size}")
            } else {
                //设置曝光
                //当天设置过就不需要设置了
                if (queryData.any { it.showDate == Utills.todayDateNum() }) {
                    Utills.elog("当天设置过就不需要设置了 ${emotionSet.typeId}, ${emotionSet.typeName ?: ""}")
                    return
                }
                //同一类 数据 共享曝光 随机分配
                //找到 同一类情感数据 共享曝光次数
                updateData = Utills.choseWhoTobeShow(queryData).onEach { it.showDate = Utills.todayDateNum() }
                Utills.elog("同一类 数据 共享曝光 随机分配 ${emotionSet.typeId}, ${emotionSet.typeName ?: ""}，${updateData.size}")
            }
            if (updateData.isNotEmpty()) {//更新到数据库
                emotionDao.update(updateData)
            }
        }
    }


    /**
     * 根据需要重置 情感数据
     * <b> 显示时间不为空且不是今天的全部需要重置
     *  <li>1，动态情感数据：（包括推送的）显示时间如果不是今天 就移除曝光 移除显示时间 处理昨天的推送还没显示的情况</li>
     *  <li>2，静态情感数据：显示时间>0且不是今天的数据，曝光为初始值 移除显示时间</li>
     *  </b>
     * @return
     */
    fun resetMagic() {
        //这里查到的动态情感只有要曝光的那几条
        //这里查到的静态情感数据 包括同一类全部的数据
        val queryResetData = emotionDao.queryResetData(Utills.todayDateNum())
        if (queryResetData.isNotEmpty()) {
            Utills.elog("resetMagic >> ${queryResetData.size}")
            //动态数据 剩余曝光清除即可
            val dynamicEmotion = queryResetData.filter { it.isDynamic() }.onEach {
                //移除显示时间
                it.showDate = 0
                //更新剩余曝光值
                it.exposure = 0
            }

            //静态情感数据 清除显示时间，分配重置曝光
            val staticEmotion = queryResetData.asSequence()
                .filter { !it.isDynamic() }
                .onEach {
                    it.exposure = 0
                    it.showDate = 0
                }.groupBy { it.typeId }
                .map {
                    Utills.choseWhoTobeShow(it.value)
                }.flatten()

            emotionDao.update(dynamicEmotion + staticEmotion)
        }
    }

    /**
     * 1，更具曝光>0 + 权重最大 找到要显示的数据
     * 2，剩余曝光-1
     * 3，更新显示时间为今天 (重置时候用)
     * 4，更新到数据库
     */
    fun perceptMagic(): EmotionData? {
        //1  查到的是不是同类的所有数据 只是部分数据
//        val queryShowData = emotionDao.queryShowData()
        val queryShowData = emotionDao.queryShowDataOneType()
        if (queryShowData.isNotEmpty()) {

            val filterCanShow = queryShowData.filter { it.exposure > 0 }
            //一类数据中 随机选一条来曝光
            val random = filterCanShow.random()

            //剩余曝光自减1
            random.exposure -= 1
            Utills.elog("perceptMagic fond data ${random.typeName} ${queryShowData.size}  $random")

            if (!random.isDynamic()) {
                //因为静态曝光的数据中 同一类只有部分可曝光 所以查出来的曝光的数据并不是一类所有数据
                //静态数据 要把同类所有数据 显示时间设置为今天 表示这一类曝光了  下次重置的时候需要用 一类中随即分别曝光
                queryShowData.onEach { it.showDate = Utills.todayDateNum() }
                Utills.elog("perceptMagic fond static data ${queryShowData.size}")
                //更新到数据库
                emotionDao.update(queryShowData)
            } else {
                //动态数据不需要 更新显示时间为今天 之前检测的时候就已经更新了
                //更新到数据库
                emotionDao.update(random)
            }
            return random
        }else{
           return emotionDao.unlimitedEmotion
        }
    }
}