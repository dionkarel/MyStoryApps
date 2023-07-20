package com.example.mystoryapps

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mystoryapps.data.entity.UserModel
import com.example.mystoryapps.response.LoginResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun saveUser(value: UserModel) {
        editor.putString(USER_ID, value.userId)
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUserId(): String {
        return preferences.getString(USER_ID, "") ?: ""
    }

    fun getName(): String {
        return preferences.getString(NAME, "") ?: ""
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