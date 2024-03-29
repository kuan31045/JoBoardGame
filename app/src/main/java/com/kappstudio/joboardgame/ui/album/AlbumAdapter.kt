package com.kappstudio.joboardgame.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.databinding.ItemPhotoAlumbBinding

class AlbumAdapter(private val viewModel: AlbumViewModel) :
    ListAdapter<String, AlbumAdapter.AlbumViewHolder>(DiffCallback) {

    inner class AlbumViewHolder(private val binding: ItemPhotoAlumbBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String, viewModel: AlbumViewModel, position: Int) {
            bindImage(binding.ivPhoto, url)
            binding.ivPhoto.setOnClickListener {
                viewModel.navToPhoto(position)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemPhotoAlumbBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, position)
    }
}