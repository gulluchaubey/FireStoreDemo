package com.example.firestoredemo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException

private const val TAG = "AndroidUtils"

fun Context.showMessageInternet(e:Exception){
    try{
        if(e.message.toString().contains("failed to connect")){
            showToasts("Please check your internet connection and try again.")
        }else{
            Log.e(TAG, "Oops! There is a problem connecting to the server. Please try again.")
        }
    }catch (e:IOException){
        Log.e(TAG, "Canceled")
    }catch (e:SocketTimeoutException){
        showToasts("Please check your internet connection and try again.")
    }catch (e:Exception){
        showToasts("Oops! There is a problem connecting to the server. Please try again.")
    }
}


fun Context.showToasts(message: String){
    var toast: Toast? = null
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

    if(toast?.view != null) {
        val toastLayout = toast.view as LinearLayout?
        val toastText = toastLayout?.getChildAt(0) as TextView
        toastText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
    }
    toast?.show()
}

fun visibiltiyGone(view: View?){
    view!!.visibility = View.GONE
}
fun visibiltiyVisible(view: View?){
    view!!.visibility = View.VISIBLE
}


fun setStatusBarColorFromActivity(context: Context, pActivity: Activity, color: Int){
    setSystemBarTheme(pActivity, false)
    pActivity.window.statusBarColor = ContextCompat.getColor(context, color)
}


fun setSystemBarTheme(pActivity: Activity, pIsDark: Boolean) {
    val lFlags = pActivity.window.decorView.systemUiVisibility
    pActivity.window.decorView.systemUiVisibility =
        if (pIsDark) lFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else lFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    fun setFullScreen(context: Context) {
        setWindowFlag(context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        context.window.statusBarColor = Color.TRANSPARENT
        setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false)
    }




fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun hideKeyBoard(context: Context) {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if ((context as Activity).currentFocus != null && context.currentFocus!!
            .windowToken != null) {
        manager.hideSoftInputFromWindow(
            context.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}


fun isNetworkAvailable(context: Context): Boolean {
    try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            return true
        }
    }catch (e:SocketTimeoutException){
        Log.e(TAG, "unable to check is internet Available")
    }catch (e: Exception) {
        try{
            Log.e(TAG, "unable to check is internet Available", e)
        }catch(e:Exception){
            Log.e(TAG, "unable to check is internet Available")
        }
    }
    return false
}
fun getRootDirPath(context: Context): String {
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        val file: File = ContextCompat.getExternalFilesDirs(
            context.applicationContext,
            null
        )[0]
        file.absolutePath
    } else {
        context.applicationContext.filesDir.absolutePath
    }
}