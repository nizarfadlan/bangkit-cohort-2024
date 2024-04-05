package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.remote.response.NewsResponse
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    private val _searchNews = MutableLiveData<NewsResponse>()
    val searchNews: LiveData<NewsResponse> = _searchNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    init {
        getNews()
    }

    private fun getNews() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNews(
            QUERY_NEWS,
            QUERY_CATEGORY,
            QUERY_LANGUAGE,
            BuildConfig.API_KEY_NEWS
        )
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchNews.value = response.body()
                } else {
                    _isError.value = response.message()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun insertHistory(historyEntity: HistoryEntity) {
        mHistoryRepository.insertHistory(historyEntity)
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        mHistoryRepository.deleteHistory(historyEntity)
    }

    companion object {
        private const val QUERY_NEWS = "cancer"
        private const val QUERY_CATEGORY = "health"
        private const val QUERY_LANGUAGE = "en"
    }
}