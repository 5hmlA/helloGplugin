@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package jzy.spark.expand

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import jzy.spark.godContext
import java.lang.ref.WeakReference
import java.util.*

/**
 * <pre>
 *     author: dhl
 *     date  : 2021/6/20
 *     desc  :
 * </pre>
 */

// toast
@kotlin.internal.InlineOnly
inline fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@kotlin.internal.InlineOnly
inline fun Context.toast(@StringRes stringResId: Int) {
    Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
}

@kotlin.internal.InlineOnly
inline fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@kotlin.internal.InlineOnly
inline fun Context.toastLong(@StringRes stringResId: Int) {
    Toast.makeText(this, getString(stringResId), Toast.LENGTH_LONG).show()
}

// 屏幕宽度(px)
inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

// 屏幕高度(px)
inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

// 屏幕的密度
inline val Context.density: Float
    get() = resources.displayMetrics.density

// dp to px
@kotlin.internal.InlineOnly
inline fun Context.dp2px(value: Int): Int = (density * value).toInt()

// px to dp
@kotlin.internal.InlineOnly
inline fun Context.px2dp(value: Int): Float = value.toFloat() / density

/**
 * 设置状态栏的颜色
 *
 * usage：
 * setSatatusBarColor(android.R.color.darker_gray)
 */
fun Context.setSatatusBarColor(@ColorRes colorResId: Int) {

    if (this is Activity) {
        setSatatusBarColor(WeakReference<Activity>(this), colorResId)
    }
}

private fun Context.setSatatusBarColor(context: WeakReference<Activity>, @ColorRes colorResId: Int) {
    context.get()?.run {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(colorResId)
        }
    }
}

// 获取 Drawable
@kotlin.internal.InlineOnly
inline fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

// 获取 color
@kotlin.internal.InlineOnly
inline fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)


@kotlin.internal.InlineOnly
inline fun Context.string(@StringRes str: Int) = this.getString(str)

@kotlin.internal.InlineOnly
inline fun Context.string(strName: String) = string(stringRes(strName))

@kotlin.internal.InlineOnly
inline fun Context.stringRes(strName: String): Int = godContext.resources.getIdentifier(strName, "string", packageName)

@kotlin.internal.InlineOnly
inline fun Context.drawable(draName: String) = godContext.resources.getIdentifier(draName, "drawable", packageName)

@kotlin.internal.InlineOnly
inline fun Context.quantity(@PluralsRes id: Int, quantity: Int, vararg args: Any?) {
    godContext.resources.getQuantityString(id, quantity, args)
}


