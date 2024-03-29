package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.ListHistoryAdapter
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private val listAdapter by lazy { ListHistoryAdapter { data -> moveToResult(data) } }
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = getString(R.string.history_title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.apply {
            rvHistory.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(this@HistoryActivity)
            }
        }

        historyViewModel.getAllHistory()
        historyViewModel.history.observe(this) { listHistory ->
            listAdapter.listHistory = listHistory
            showTVNoHistory(listHistory.isEmpty())
        }
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.getAllHistory()
    }

    private fun showTVNoHistory(isNotFound: Boolean) {
        binding.tvNoHistory.visibility = if (isNotFound) View.VISIBLE else View.GONE
    }

    private fun moveToResult(data: HistoryEntity) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_HISTORY, ResultActivity.HISTORY_REQUEST_CODE)
        intent.putExtra(ResultActivity.EXTRA_LOCAL_DATA, data)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}