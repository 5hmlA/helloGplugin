package com.google.android.material.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat

/***********************************************************
 ** Copyright (C), 2008-2016, OPPO Mobile Comm Corp., Ltd.

 ** File: jzy.spark.demo.ui
 ** Description: tellu
 ** Version: 1.0
 ** Date : 2021/7/6
 ** Author: jiangzubin@oppo.com
 **
 ** ---------------------Revision History: ---------------------
 **      <author>           <data>        <version >     <desc>
 ** jiangzubin@oppo.com    2021/7/6          1.0
 ****************************************************************/
class CollapsingToolbarLayoutFit @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CollapsingToolbarLayout(context, attrs, defStyleAttr) {


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ViewCompat.setFitsSystemWindows(this, true)
    }

}