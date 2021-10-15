package com.dicoding.dummyusersearch.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")
    private val splashKey = booleanPreferencesKey("splash_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    fun getSplashscreenSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[splashKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isSplashscreenActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isSplashscreenActive
        }
    }

    suspend fun saveSplashscreenSetting(isSplashscreenActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[splashKey] = isSplashscreenActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}