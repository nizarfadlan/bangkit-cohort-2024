package com.nizarfadlan.submissionakhiraplikasiakhir

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nizarfadlan.submissionakhiraplikasiakhir.databinding.ActivityDetailCourseBinding
import com.nizarfadlan.submissionakhiraplikasiakhir.databinding.ChipTagsCourseBinding
import com.nizarfadlan.submissionakhiraplikasiakhir.model.Course
import io.noties.markwon.Markwon


class DetailCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCourseBinding
    private var course: Course? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        course = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_COURSE, Course::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_COURSE)
        }

        supportActionBar?.apply {
            title = "Course Detail"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (course != null) {
            setDetailCourse(course!!)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_course, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setDetailCourse(course: Course) {
        val (title, description, thumbnail, timeLearn, tags, stars) = course
        binding.tvTitleCourse.text = title
        binding.tvStarCourse.text = stars.toString()
        binding.tvTimeCourse.text = timeLearn.toString().plus(" Hours")

        Markwon.create(this).apply {
            setMarkdown(binding.tvDescriptionCourse, description)
        }

        for (tag in tags) {
            val chip = ChipTagsCourseBinding.inflate(LayoutInflater.from(this), null, false).root
            chip.text = tag
            chip.isClickable = false
            binding.tagsCourse.addView(chip)
        }

        Glide.with(this)
            .load(thumbnail)
            .error(R.drawable.broken_image_24px)
            .into(binding.imgDetailCourse)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_share -> {
                shareCourse(course!!)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareCourse(course: Course) {
        val (title, description, thumbnail) = course

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, thumbnail)
            putExtra(Intent.EXTRA_TEXT, "Course: \n$title\n\nDescription: \n$description")
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Course"))
    }

    companion object {
        const val EXTRA_COURSE = "extra_course"
    }
}