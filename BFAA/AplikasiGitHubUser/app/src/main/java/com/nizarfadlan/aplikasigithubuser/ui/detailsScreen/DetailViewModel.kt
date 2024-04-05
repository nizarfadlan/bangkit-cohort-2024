package com.nizarfadlan.aplikasigithubuser.ui.detailsScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.aplikasigithubuser.data.GithubRepository
import com.nizarfadlan.aplikasigithubuser.data.Result
import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity
import com.nizarfadlan.aplikasigithubuser.data.remote.response.ItemsItem
import kotlinx.coroutines.launch

class DetailViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _detailUser = MutableLiveData<Result<UserEntity>>()
    val detailUser: LiveData<Result<UserEntity>> by lazy { _detailUser }

    private val _followersUser = MutableLiveData<Result<List<ItemsItem>>>()
    val followersUser: LiveData<Result<List<ItemsItem>>> by lazy { _followersUser }

    private val _followingUser = MutableLiveData<Result<List<ItemsItem>>>()
    val followingUser: LiveData<Result<List<ItemsItem>>> by lazy { _followingUser }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            githubRepository.getDetailUser(username).collect {
                _detailUser.postValue(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            githubRepository.getFollowers(username).collect {
                _followersUser.postValue(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            githubRepository.getFollowing(username).collect {
                _followingUser.postValue(it)
            }
        }
    }

    fun updateFavoriteUser(isFavorite: Boolean, username: String) {
        viewModelScope.launch {
            githubRepository.updateFavoriteUser(isFavorite, username)
            Log.d(TAG, "updateFavoriteUser: $username")
        }
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}