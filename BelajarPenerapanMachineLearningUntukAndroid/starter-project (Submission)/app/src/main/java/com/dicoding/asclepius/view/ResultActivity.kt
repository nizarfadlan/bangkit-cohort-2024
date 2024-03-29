package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.ListNewsAdapter
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.DateHelper

class ResultActivity : AppCompatActivity() {
    private val resultViewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private val listAdapter by lazy { ListNewsAdapter { url -> moveToNews(url) } }
    private lateinit var binding: ActivityResultBinding

    private var historyEntity: HistoryEntity? = HistoryEntity()
    private var requestCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = getString(R.string.result_title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.apply {
            rvNewsHeadline.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(this@ResultActivity)
            }
        }

        requestCode = intent.getIntExtra(EXTRA_HISTORY, 0)

        if (requestCode == HISTORY_REQUEST_CODE) {
            historyEntity = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_LOCAL_DATA, HistoryEntity::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_LOCAL_DATA)
            }
        } else {
            historyEntity?.apply {
                imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
                prediction = intent.getStringExtra(EXTRA_PREDICTION) ?: ""
                confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE, 0f)
                date = DateHelper.getCurrentDate()
            }
        }

        historyEntity?.let { history ->
            history.imageUri.let {
                binding.resultImage.setImageURI(Uri.parse(it))
            }
            val predictionAndConfidence = String.format(
                "%s (%.2f%%)", history.prediction, history.confidenceScore
            )
            binding.resultText.text = getString(R.string.result, predictionAndConfidence)
        }

        setNewsData()
    }

    private fun setNewsData() {
        resultViewModel.isLoading.observe(this, ::showLoading)
        resultViewModel.searchNews.observe(this) { newsResponse ->
            if (newsResponse.totalResults > 0) {
                listAdapter.listNews = newsResponse.articles
            } else {
                showTVNoResult(getString(R.string.no_results_news))
            }
        }

        resultViewModel.isError.observe(this) {
            val message = getString(R.string.error_result_news, it)
            showTVNoResult(message)
        }
    }

    private fun moveToNews(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showTVNoResult(message: String) {
        binding.tvNoResult.visibility = View.VISIBLE
        binding.tvNoResult.text = message
    }

    private fun saveResult() {
        historyEntity?.let { resultViewModel.insertHistory(it) }
        showToast(getString(R.string.save_history))
    }

    private fun deleteHistory() {
        historyEntity?.let { resultViewModel.deleteHistory(it) }
        if (historyEntity != null) {
            showToast(getString(R.string.delete_history))
            finish()
        } else showToast(getString(R.string.error_delete_history))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.result_menu, menu)
        if (requestCode == HISTORY_REQUEST_CODE) {
            menu?.findItem(R.id.menu_save)?.isVisible = false
        } else {
            menu?.findItem(R.id.menu_delete)?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.menu_save -> {
                saveResult()
            }

            R.id.menu_delete -> {
                deleteHistory()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICTION = "extra_prediction"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_LOCAL_DATA = "extra_local_data"
        const val EXTRA_HISTORY = "extra_history"
        const val HISTORY_REQUEST_CODE = 100
    }
}