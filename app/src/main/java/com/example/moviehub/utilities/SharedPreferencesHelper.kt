package com.example.moviehub.utilities

import android.content.Context
import android.content.SharedPreferences
import com.example.moviehub.utilities.Constants.Companion.PREF_NAME

object SharedPreferencesHelper {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

     // Initializes the shared preferences object.
    fun initSharedPreferences(application: Context) {
        sharedPreferences = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

     // Saves a value to shared preferences.
    fun saveValue(key: String, value: Any): Boolean {
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw UnsupportedOperationException("Unsupported type: ${value::class.java}")
        }
        return editor.commit()
    }
     // Retrieves a value from shared preferences.
     fun <T : Any> getValue(key: String, defaultValue: T): Comparable<*> {
         return when (defaultValue) {
             is String -> sharedPreferences.getString(key, defaultValue)!!
             is Int -> sharedPreferences.getInt(key, defaultValue)
             is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
             is Float -> sharedPreferences.getFloat(key, defaultValue)
             is Long -> sharedPreferences.getLong(key, defaultValue)
             else -> throw UnsupportedOperationException("Unsupported type: ${defaultValue::class.java}")
         }
     }
}