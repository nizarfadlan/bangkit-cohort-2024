package com.nizarfadlan.aplikasigithubuser.ui.settingScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.aplikasigithubuser.data.GithubRepository
import com.nizarfadlan.aplikasigithubuser.utils.ThemeMode
import kotlinx.coroutines.launch

class SettingViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    fun getThemeSettings(): LiveData<ThemeMode> = githubRepository.getThemeSettings().asLiveData()

    fun saveThemeSetting(themeMode: ThemeMode): Unit {
        viewModelScope.launch {
            githubRepository.saveThemeSetting(themeMode)
            Log.d(TAG, "saveThemeSetting: ${themeMode.name}")
        }
    }

    companion object {
        private const val TAG = "SettingViewModel"
    }
}