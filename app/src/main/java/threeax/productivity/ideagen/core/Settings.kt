package threeax.productivity.ideagen.core

import android.content.Context

class Settings(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun initiate() {
        if (!sharedPreferences.contains("checksum")) {
            // Initialize settings with default values
            with(sharedPreferences.edit()) {
                AppData.settingsData.forEach {
                    putString(it.key, it.value)
                }
                putString("checksum", "1")
                apply()
            }
        }
    }

    fun saveSettings() {
        with(sharedPreferences.edit()) {
            AppData.settingsData.forEach {
                putString(it.key, it.value)
            }
            apply()
        }
    }

    fun loadSettings() {
        AppData.settingsData.forEach {
            it.value = sharedPreferences.getString(it.key, it.value) ?: it.value
        }
    }

    fun getSetting(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun setSetting(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(id: String, value: String): Any {
        return sharedPreferences.getString(id, value) ?: value
    }
}