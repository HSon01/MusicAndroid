package com.ssn.sxmusic.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("StaticFieldLeak")
object PrefControllerSingleton {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context


    fun prefController(c: Context) {
        context = c
        sharedPreferences =
            context.getSharedPreferences(Const.PREFERENCE_SharedPreferences, Context.MODE_PRIVATE)
    }

    fun getMediaLoop(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun setMediaLoop(key: String, value: Int) {
        val edit = sharedPreferences.edit()
        edit.putInt(key, value)
        edit.apply()
    }

}