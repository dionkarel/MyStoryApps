package com.example.mystoryapps

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapps.data.entity.StoriesEntity
import com.example.mystoryapps.databinding.StoryListBinding
import com.example.mystoryapps.ui.DetailActivity
import com.example.mystoryapps.utils.Utils

class StoriesAdapter :
    PagingDataAdapter<StoriesEntity, StoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val storyBinding =
            StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(storyBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(data)
            }
        }
    }

    inner class ListViewHolder(private val binding: StoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context = itemView.context!!
        fun bind(data: StoriesEntity) {
            with(binding) {
                tvGetUsername.text = data.name
                tvGetDatePost.text = Utils.dateFormat(data.createdAt)
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(binding.ivGetStory)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: StoriesEntity)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoriesEntity> =
            object : DiffUtil.ItemCallback<StoriesEntity>() {

                override fun areItemsTheSame(
                    oldItem: StoriesEntity,
                    newItem: StoriesEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: StoriesEntity,
                    newItem: StoriesEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}