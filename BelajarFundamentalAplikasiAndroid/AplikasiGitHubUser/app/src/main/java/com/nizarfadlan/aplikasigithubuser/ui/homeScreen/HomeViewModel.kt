package com.nizarfadlan.aplikasigithubuser.ui.homeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.aplikasigithubuser.data.GithubRepository
import com.nizarfadlan.aplikasigithubuser.data.Result
import com.nizarfadlan.aplikasigithubuser.data.remote.response.SearchUserResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _searchUser = MutableLiveData<Result<SearchUserResponse>>()
    val searchUser: LiveData<Result<SearchUserResponse>> by lazy { _searchUser }

    init {
        getAllUsers(GITHUB_USERNAME_DEFAULT)
    }

    fun getAllUsers(username: String) {
        viewModelScope.launch {
            githubRepository.getAllUsers(username).collect {
                _searchUser.postValue(it)
            }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
        const val GITHUB_USERNAME_DEFAULT = "nizar"
    }
}