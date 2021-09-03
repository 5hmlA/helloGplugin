@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
package jzy.spark.expand

import android.content.res.Resources
import android.provider.Settings
import android.util.Log
import android.util.TimeUtils
import android.util.TypedValue
import jzy.spark.godContext
import java.util.concurrent.TimeUnit

/**
 * @author yun.
 * @date 2021/8/13
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */


inline val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        godContext.resources.displayMetrics
    )

inline val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        godContext.resources.displayMetrics
    )


inline val Float.sdp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

inline val Int.sdp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

@kotlin.internal.InlineOnly
inline fun String.log() {
    Log.d("TAG", this)
}

@kotlin.internal.InlineOnly
inline fun Long.sec2Day(): Long {
    return TimeUnit.SECONDS.toDays(this)
}

@kotlin.internal.InlineOnly
inline fun Long.mil2Minute(): Long {
    return TimeUnit.MILLISECONDS.toMinutes(this)
}

