package com.nizarfadlan.aplikasigithubuser.ui.favoriteScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.aplikasigithubuser.data.GithubRepository
import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _favoriteUser = MutableLiveData<List<UserEntity>>()
    val favoriteUser: LiveData<List<UserEntity>> by lazy { _favoriteUser }

    fun getAllFavoriteUsers() {
        viewModelScope.launch {
            githubRepository.getAllFavoriteUsers().collect {
                _favoriteUser.postValue(it)
            }
        }
    }
}