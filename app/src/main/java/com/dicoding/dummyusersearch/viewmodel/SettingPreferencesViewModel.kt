package com.dicoding.dummyusersearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dummyusersearch.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingPreferencesViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun getSplashscreenSettings(): LiveData<Boolean> {
        return pref.getSplashscreenSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun saveSplashscreenSetting(isSplashscreenActive: Boolean) {
        viewModelScope.launch {
            pref.saveSplashscreenSetting(isSplashscreenActive)
        }
    }
}