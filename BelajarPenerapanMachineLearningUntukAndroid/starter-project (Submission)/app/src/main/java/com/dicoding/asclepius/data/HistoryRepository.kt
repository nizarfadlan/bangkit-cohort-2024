package com.dicoding.asclepius.data

import android.app.Application
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao
import com.dicoding.asclepius.data.local.room.HistoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryDatabase.getInstance(application)
        mHistoryDao = db.historyDao()
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> = flow {
        emit(mHistoryDao.getAllHistory())
    }

    fun insertHistory(historyEntity: HistoryEntity) {
        executorService.execute { mHistoryDao.insertHistory(historyEntity) }
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        executorService.execute { mHistoryDao.deleteHistory(historyEntity) }
    }
}