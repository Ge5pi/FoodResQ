package com.example.foodresq.adaptersEtc

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveLoginState(isLoggedIn: Boolean, login: String) {
        with(prefs.edit()) {
            putBoolean("is_logged_in", isLoggedIn)
            putString("user_login", login)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun getUserEmail(): String? {
        return prefs.getString("user_email", null)
    }

    fun getUserLogin(): String? {
        return prefs.getString("user_login", null)
    }


    fun clearLoginState() {
        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}
