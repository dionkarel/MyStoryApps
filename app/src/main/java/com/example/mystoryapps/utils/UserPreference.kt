package com.example.mystoryapps.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mystoryapps.data.local.entity.UserModel

class UserPreference (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun saveUser(value: UserModel) {
        editor.putString(USER_ID, value.userId)
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getToken(): String {
        return preferences.getString(TOKEN, "") ?: ""
    }

    fun userLogout() {
        editor.clear()
        editor.apply()
    }


    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val USER_ID = "user_id"
        private const val NAME = "name"
        private const val TOKEN = "token"
    }
}