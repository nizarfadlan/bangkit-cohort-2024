package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemsHistoryBinding
import kotlin.properties.Delegates

class ListHistoryAdapter(
    private val onItemClickCallback: (HistoryEntity) -> Unit? = { _ -> }
) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {
    var listHistory: List<HistoryEntity> by Delegates.observable(emptyList()) { _, oldList, newList ->
        notifyChanges(oldList, newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemsHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listHistory[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listHistory.size

    inner class ListViewHolder(private val binding: ItemsHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: HistoryEntity) {
            with(binding) {
                tvDate.text = news.date

                root.setOnClickListener {
                    onItemClickCallback.invoke(news)
                }
            }
        }
    }

    private fun notifyChanges(oldList: List<HistoryEntity>, newList: List<HistoryEntity>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this)
    }
}