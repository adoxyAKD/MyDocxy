package com.example.mydocxy.apiDataManager.ui


import android.content.Context
import android.content.SharedPreferences



class NotificationPreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications_enabled", true)
    }
}
