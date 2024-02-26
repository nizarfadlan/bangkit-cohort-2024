package com.nizarfadlan.submissionakhiraplikasiakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.submissionakhiraplikasiakhir.adapter.ListCourseAdapter
import com.nizarfadlan.submissionakhiraplikasiakhir.databinding.ActivityMainBinding
import com.nizarfadlan.submissionakhiraplikasiakhir.model.Course
import com.nizarfadlan.submissionakhiraplikasiakhir.model.CourseData

class MainActivity : AppCompatActivity() {
    private lateinit var rvCourse: RecyclerView
    private val list = ArrayList<Course>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvCourse = binding.rvCourse
        rvCourse.setHasFixedSize(true)

        list.addAll(CourseData.listData)
        showRecyclerList()

        supportActionBar?.title = "Course"
    }

    private fun showRecyclerList() {
        rvCourse.layoutManager = LinearLayoutManager(this)
        val listCourseAdapter = ListCourseAdapter(list)
        rvCourse.adapter = listCourseAdapter

        listCourseAdapter.setOnItemClickCallback(object : ListCourseAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Course) {
                moveDetailCourse(data)
            }
        })
    }

    private fun moveDetailCourse(data: Course) {
        val moveIntent = Intent(this@MainActivity, DetailCourseActivity::class.java)
        moveIntent.putExtra(DetailCourseActivity.EXTRA_COURSE, data)
        startActivity(moveIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_page -> {
                val moveIntent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}