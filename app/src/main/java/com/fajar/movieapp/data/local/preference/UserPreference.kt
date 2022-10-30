package com.fajar.movieapp.data.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreference(context: Context) {
    private val preference: SharedPreferences = context.getSharedPreferences(NAME, MODE)

    var loginKeyValue: Boolean
        get() = preference.getBoolean(
            PreferenceKey.PREF_USER_LOGIN_KEY.first,
            PreferenceKey.PREF_USER_LOGIN_KEY.second,
        )
        set(value) = preference.edit {
            this.putBoolean(PreferenceKey.PREF_USER_LOGIN_KEY.first, value)
        }

    var userIdKeyValue: Long
        get() = preference.getLong(
            PreferenceKey.PREF_USER_ID_KEY.first,
            PreferenceKey.PREF_USER_ID_KEY.second,
        )
        set(value) = preference.edit {
            this.putLong(PreferenceKey.PREF_USER_ID_KEY.first, value)
        }

    companion object {
        private const val NAME = "LoginAuth"
        private const val MODE = Context.MODE_PRIVATE
    }
}

object PreferenceKey{
    val PREF_USER_LOGIN_KEY = Pair("PREF_USER_LOGIN_KEY", false)
    val PREF_USER_ID_KEY = Pair("PREF_USER_ID_KEY", 0L)
}