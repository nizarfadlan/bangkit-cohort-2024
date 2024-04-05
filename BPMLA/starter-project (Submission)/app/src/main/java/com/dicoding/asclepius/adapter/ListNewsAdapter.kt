package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding
import kotlin.properties.Delegates

class ListNewsAdapter(
    private val onItemClickCallback: (String) -> Unit? = { _ -> }
) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listNews[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listNews.size

    inner class ListViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {
            with(binding) {
                tvTitle.text = news.title
                tvDescription.text = news.description
                Glide.with(root.context)
                    .load(news.urlToImage)
                    .error(R.drawable.ic_place_holder)
                    .into(ivItem)

                root.setOnClickListener {
                    onItemClickCallback.invoke(news.url)
                }
            }
        }
    }

    private fun notifyChanges(oldList: List<ArticlesItem>, newList: List<ArticlesItem>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].title == newList[newItemPosition].title
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this)
    }

    var listNews: List<ArticlesItem> by Delegates.observable(emptyList()) { _, oldList, newList ->
        notifyChanges(oldList, newList)
    }
}