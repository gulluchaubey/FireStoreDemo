package com.example.firestoredemo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.firestoredemo.home.HomeActivity
import com.example.firestoredemo.login.LoginActivity
import com.example.firestoredemo.utils.ConstantUtils
import com.example.firestoredemo.utils.PrefHelper
import com.example.firestoredemo.utils.setFullScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            when {
                PrefHelper(this@SplashActivity).getBoolean(ConstantUtils.IS_LOGIN) -> {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }

        }
    }
}