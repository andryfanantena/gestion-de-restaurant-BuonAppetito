package com.teamsasa.buonappetito.data.local

import android.content.Context
import android.content.SharedPreferences
import com.teamsasa.buonappetito.utils.Constants

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit().putString(Constants.KEY_TOKEN, token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(Constants.KEY_TOKEN, null)
    }

    fun clearSession() {
        prefs.edit().remove(Constants.KEY_TOKEN).apply()
    }
}
