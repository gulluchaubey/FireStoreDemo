package com.example.firestoredemo.utils

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(var context: Context) {
     val preferences: SharedPreferences = context.getSharedPreferences(ConstantUtils.APP_PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor  = preferences.edit()

    fun clearPreferences() = editor.clear().commit()

    fun saveString(key: String, value: String) = editor.putString(key, value).commit()

    fun getString(key: String): String{
        return preferences.getString(key, "").toString()
    }
    fun saveBoolean(key : String, value : Boolean) = editor.putBoolean(key,value).commit()

    fun getBoolean(key: String) : Boolean {
        return preferences.getBoolean(key, false)
    }

}