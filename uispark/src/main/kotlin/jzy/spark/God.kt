@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package jzy.spark

import android.app.ActivityThread
import android.app.AppGlobals
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleRegistry
import jzy.spark.expand.log
import java.util.*

/**
 * @author yun.
 * @date 2021/8/13
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */

//反射库 kotlin好用扩展

val processName: String by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Application.getProcessName()
    } else {
        //这个方法在android4.3.1上就已经有了
        ActivityThread.currentProcessName()
    }
}

val packageName: String by lazy {
    godContext.packageName
}

val godContext: Application by lazy {
    val application = AppGlobals.getInitialApplication() ?: ActivityThread.currentApplication()
    application?.also {
        it.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration) {
                "onConfigurationChanged >> $isPhoneState_".log()
            }

            override fun onLowMemory() {
                "onLowMemory >> ".log()
            }
        })
    }
    application
//    AppGlobals.getInitialApplication() ?: ActivityThread.currentApplication() ?: throw RuntimeException("context error")
}

val isPhone: Boolean by lazy {
    val displayMetrics = godContext.resources.displayMetrics
    val min = displayMetrics.widthPixels.coerceAtMost(displayMetrics.heightPixels)
    val max = displayMetrics.widthPixels.coerceAtLeast(displayMetrics.heightPixels)
    val proportion = min * 1F / max
    //0.75，0.66666，0.6，0.56，0.46  ，0.45
    proportion <= 0.75F
}

val isPhoneState: Boolean
    get() = isPhoneState_


private val isPhoneState_: Boolean
    get() {
        val displayMetrics = godContext.resources.displayMetrics
        val min = displayMetrics.widthPixels.coerceAtMost(displayMetrics.heightPixels)
        val max = displayMetrics.widthPixels.coerceAtLeast(displayMetrics.heightPixels)
        val proportion = min * 1F / max
        //0.75，0.66666，0.6，0.56，0.46  ，0.45
        return proportion <= 0.75F
    }


val mainHandler: Handler = object : Handler(Looper.getMainLooper()) {
    var toastShowing = false
    val toast by lazy {
        Toast.makeText(godContext, "jspark", Toast.LENGTH_SHORT).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                it.addCallback(object : Toast.Callback() {
                    override fun onToastShown() {
                        toastShowing = true
                    }

                    override fun onToastHidden() {
                        toastShowing = false
                    }
                })
            }
        }
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what != 0) {
            return
        }
        if (msg.obj is CharSequence) {
            toast.setText(msg.obj as CharSequence)
        } else {
            toast.setText(msg.obj as Int)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            toastShowing = if (toast.view != null) {
                toast.view!!.isShown
            } else {
                false
            }
        }
        if (!toastShowing) {
            toast.show()
        }
    }
}

@kotlin.internal.InlineOnly
inline fun jtoast(msg: CharSequence) {
    mainHandler.sendMessage(Message.obtain().also {
        it.obj = msg
    })
}

@kotlin.internal.InlineOnly
inline fun jtoast(@StringRes strInt: Int) {
    mainHandler.sendMessage(Message.obtain().also {
        it.obj = strInt
    })
}

@kotlin.internal.InlineOnly
inline fun runOnUiThread(action: Runnable) {
    mainHandler.post(action)
}