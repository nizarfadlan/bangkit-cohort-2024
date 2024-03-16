package com.nizarfadlan.aplikasigithubuser.data

import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity
import com.nizarfadlan.aplikasigithubuser.data.local.room.UserDao
import com.nizarfadlan.aplikasigithubuser.data.remote.response.ItemsItem
import com.nizarfadlan.aplikasigithubuser.data.remote.response.SearchUserResponse
import com.nizarfadlan.aplikasigithubuser.data.remote.retrofit.ApiService
import com.nizarfadlan.aplikasigithubuser.utils.SettingPreferences
import com.nizarfadlan.aplikasigithubuser.utils.ThemeMode
import com.nizarfadlan.aplikasigithubuser.utils.toUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GithubRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val pref: SettingPreferences
) {
    fun getAllUsers(username: String): Flow<Result<SearchUserResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.searchUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailUser(username: String): Flow<Result<UserEntity>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.detailUser(username)
            if (userDao.isUserIsExist(username)) {
                userDao.updateDetailUser(
                    response.id,
                    username,
                    response.name ?: "",
                    response.followers ?: 0,
                    response.following ?: 0,
                    response.avatarUrl ?: "",
                    response.bio ?: "",
                    response.htmlUrl ?: "",
                    response.email ?: ""
                )
            } else {
                userDao.insertFavorite(response.toUserEntity(username))
            }
            val user = userDao.getUser(username)
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowers(username: String): Flow<Result<List<ItemsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.followersUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowing(username: String): Flow<Result<List<ItemsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.followingUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getThemeSettings(): Flow<ThemeMode> {
        return pref.getThemeSetting()
    }

    suspend fun saveThemeSetting(themeMode: ThemeMode) {
        pref.saveThemeSetting(themeMode)
    }

    suspend fun getAllFavoriteUsers(): Flow<List<UserEntity>> = flow {
        emit(userDao.getAllFavoriteUsers())
    }

    suspend fun updateFavoriteUser(isFavorite: Boolean, username: String) {
        userDao.updateFavorite(isFavorite, username)
    }

    companion object {
        @Volatile
        private var instance: GithubRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            pref: SettingPreferences
        ): GithubRepository =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(apiService, userDao, pref)
            }.also { instance = it }
    }
}