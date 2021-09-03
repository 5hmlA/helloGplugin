package com.google.android.material.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView

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
class AppBarLayoutFit @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        println("========================")
        println((getChildAt(0) as ViewGroup).getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)))
        println(measuredHeight)
        println(totalScrollRange)
    }

}