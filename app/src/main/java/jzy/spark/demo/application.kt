package jzy.spark.demo

import android.app.Application

/***********************************************************
 * Copyright (C), 2008-2016, OPPO Mobile Comm Corp., Ltd.
 * File: op.po.tellu
 * Description: tellu
 * Version: 1.0
 * Date : 2021/7/5
 * Author: jiangzubin@oppo.com
 *
 * ---------------------Revision History: ---------------------
 * <author>           <data>        <version>     <desc>
 * jiangzubin@oppo.com    2021/7/5          1.0
 */
class application : Application(){
    override fun onCreate() {
        super.onCreate()
        Utills.keepContext(this)

//        DoraemonKit.install(this);
    }
}