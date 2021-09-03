package jzy.spark.tellu.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jzy.spark.tellu.room.dao.EmotionDao
import jzy.spark.tellu.room.table.EmotionData

/***********************************************************
 * Copyright (C), 2008-2016, OPPO Mobile Comm Corp., Ltd.
 * File: com.com.heytap.databaseengine
 * Description: TestToolForPhone
 * Version: 1.0
 * Date : 2019/12/5
 * Author: jiangzubin@oppo.com
 *
 * ---------------------Revision History: ---------------------
 * <author>           <data>        <version>     <desc>
 * jiangzubin@oppo.com    2019/12/5          1.0
</desc></version></data></author> */
@Database(entities = [EmotionData::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun emotionDao(): EmotionDao

    companion object {

        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, AppDataBase::class.java, "emotion.db").build().also {
                instance = it
            }
        }

    }
}