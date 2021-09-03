package jzy.spark.tellu.room.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EmotionTable")
data class EmotionData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,
    val contentId: Int,
    val typeId: Int,
    /**
     * 内容格式 不同id要取不同的业务数据
     * 1(%s 时间,%d 一共几天)
     * 2(%s 时间,%d 连续几天)
     * 3(%d 取步数目标)
     * 4(%s 目标完成率)
     */
    val formatId: Int,
    val typeName: String,
    val title: String,
    var tips: String,
    val weights: Int,
    /** 云端配置的固定曝光次数 为0表示无限次数曝光  */
    val exposureFix: Int,
    //本地更新的 用来记录剩余更新次数
    var exposure: Int,
    var showDate: Int,
    var extra: String?
)

