package com.nizarfadlan.aplikasigithubuser.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeMode(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system")
}

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = stringPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<ThemeMode> {
        return dataStore.data.map { preferences ->
            val themeString = preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name
            ThemeMode.valueOf(themeString)
        }
    }

    suspend fun saveThemeSetting(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeMode.name
        }
    }

    companion object {
        @Volatile
        private var instance: SettingPreferences? = null

        fun getInstance(context: Context): SettingPreferences {
            return instance ?: synchronized(this) {
                instance ?: SettingPreferences(context.dataStore).also { instance = it }
            }
        }
    }

}