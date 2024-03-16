package com.nizarfadlan.aplikasigithubuser.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.nizarfadlan.aplikasigithubuser.databinding.ActivityMainBinding
import com.nizarfadlan.aplikasigithubuser.ui.settingScreen.SettingViewModel
import com.nizarfadlan.aplikasigithubuser.utils.ThemeMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val settingViewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingViewModel.getThemeSettings().observe(this) { themeMode ->
            when (themeMode) {
                ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}