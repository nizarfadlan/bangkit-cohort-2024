package com.nizarfadlan.submissionakhiraplikasiakhir.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.nizarfadlan.submissionakhiraplikasiakhir.model.Course
import com.nizarfadlan.submissionakhiraplikasiakhir.R
import com.nizarfadlan.submissionakhiraplikasiakhir.databinding.ItemDataCourseBinding

class ListCourseAdapter(private val listCourse: ArrayList<Course>): RecyclerView.Adapter<ListCourseAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemDataCourseBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, description, thumbnail) = listCourse[position]

        Glide.with(holder.itemView.context)
            .load(thumbnail)
            .error(R.drawable.broken_image_24px)
            .into(holder.imgThumbnail)

        holder.tvTitle.text = title
        holder.tvDescription.text = description.substring(0, 85).plus("...")

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listCourse[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listCourse.size
    }

    inner class ListViewHolder(binding: ItemDataCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTitle: TextView = binding.tvItemTitle
        var tvDescription: TextView = binding.tvItemDescription
        var imgThumbnail: ShapeableImageView = binding.imgItemPhoto
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Course)
    }
}