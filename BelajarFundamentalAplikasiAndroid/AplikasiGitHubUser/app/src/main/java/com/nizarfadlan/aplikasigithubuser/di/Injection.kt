package com.nizarfadlan.aplikasigithubuser.di

import android.content.Context
import com.nizarfadlan.aplikasigithubuser.data.GithubRepository
import com.nizarfadlan.aplikasigithubuser.data.local.room.UserDatabase
import com.nizarfadlan.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.nizarfadlan.aplikasigithubuser.utils.SettingPreferences

object Injection {
    fun provideRepository(context: Context): GithubRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val pref = SettingPreferences.getInstance(context)
        val dao = database.favoriteUserDao()
        return GithubRepository.getInstance(apiService, dao, pref)
    }
}