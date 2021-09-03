package jzy.spark.tellu.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jzy.spark.tellu.room.table.EmotionData


@Dao
interface EmotionDao {
    @get:Query("SELECT * FROM EmotionTable")
    val all: List<EmotionData?>?

    @Insert
    fun insert(emotionTable: EmotionData)

    @Insert
    fun insert(emotionTable: List<EmotionData>)

    @Query("DELETE FROM EmotionTable")
    fun delTable()


    @Update
    fun update(emotionTables: EmotionData)

    @Update
    fun update(emotionTables: List<EmotionData>)

//    /**
//     * 查找权重最大(权重要>0) 同时 曝光次数>0 的所有数据(如果是app计算权重的只需要今天的，静态的情感不需要时间限制)
//     * @return
//     */
//    @Query(
//        "SELECT id, contentId, typeId, title, formatId, typeName, tips, weightsFix, weights, exposureFix, exposure, reset, extra, updateTime, MAX(weights) AS weights " +
//                "FROM  EmotionTable WHERE exposure > 0 AND weights > 0 AND (typeId < 500 OR updateTime LIKE (:today))"
//    )
//    fun getByBiggerWeights(today: String?): List<EmotionTable?>?

    /**
     * 和云端协定 固定的报告次数为0的数据 为无限显示的数据
     * @return
     */
    @get:Query("SELECT * FROM EmotionTable WHERE exposureFix == 0 ORDER BY RANDOM() LIMIT 1")
    val unlimitedEmotion: EmotionData?

    @Query("SELECT COUNT(id) AS nums FROM EmotionTable")
    fun queryCount(): Int



    @Query("SELECT * FROM EmotionTable WHERE typeId == (:typeId) OR typeName IS (:typeName)")
//    @Query("SELECT * FROM EmotionTable WHERE typeId == (:typeId) OR typeName IS (typeName)")
    fun queryData(typeId: Int, typeName: String?): List<EmotionData>

    //查询 显示时间不是今天 的数据
    @Query("SELECT * FROM EmotionTable WHERE (showDate > 0 AND showDate != (:todayDate)) ORDER BY typeId")
    fun queryResetData(todayDate: Int): List<EmotionData>

    //查找 可以曝光 且权重最大的数据 一类的数据同一个权重
//    @Query("SELECT * FROM EmotionTable WHERE exposure > 0 ORDER BY weights ")//DESC 降序
    @Query("SELECT * FROM EmotionTable WHERE weights == (SELECT MAX(weights) FROM EmotionTable WHERE exposure > 0) AND exposure > 0")
    fun queryShowData(): List<EmotionData>

    //查找 可以曝光 且权重最大的数据 一类的数据同一个权重
//    @Query("SELECT * FROM EmotionTable WHERE exposure > 0 ORDER BY weights ")//DESC 降序
    @Query("SELECT * FROM EmotionTable WHERE typeId == (SELECT typeId FROM EmotionTable WHERE weights == (SELECT MAX(weights) FROM EmotionTable WHERE exposure > 0) AND exposure > 0 ORDER BY RANDOM() LIMIT 1)")
    fun queryShowDataOneType(): List<EmotionData>

    /**
     * 查找同类所有数据 主要用在静态数据
     * @param typeId
     * @return
     */
    @Query("SELECT * FROM EmotionTable WHERE typeId is (:typeId)")
    fun getByTypeId(typeId: Int): List<EmotionData>


}