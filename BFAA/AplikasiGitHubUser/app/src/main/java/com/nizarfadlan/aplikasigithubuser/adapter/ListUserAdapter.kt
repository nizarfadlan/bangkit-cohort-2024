package com.nizarfadlan.aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nizarfadlan.aplikasigithubuser.R
import com.nizarfadlan.aplikasigithubuser.data.remote.response.ItemsItem
import com.nizarfadlan.aplikasigithubuser.databinding.ItemUserBinding
import kotlin.properties.Delegates

class ListUserAdapter(
    private val isClickable: Boolean = true,
    private val onItemClickCallback: (String) -> Unit? = { _ -> }
) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listUsers[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listUsers.size

    inner class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            with(binding) {
                tvItem.text = user.login
                Glide.with(root.context)
                    .load(user.avatarUrl)
                    .error(R.drawable.broken_image_24px)
                    .into(ivItem)

                root.apply {
                    isClickable = this@ListUserAdapter.isClickable
                    elevation = if (!this@ListUserAdapter.isClickable) 0f else 8f
                    if (isClickable) {
                        setOnClickListener {
                            onItemClickCallback.invoke(user.login)
                        }
                    }
                }
            }
        }
    }

    private fun notifyChanges(oldList: List<ItemsItem>, newList: List<ItemsItem>) {
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

    var listUsers: List<ItemsItem> by Delegates.observable(emptyList()) { _, oldList, newList ->
        notifyChanges(oldList, newList)
    }
}