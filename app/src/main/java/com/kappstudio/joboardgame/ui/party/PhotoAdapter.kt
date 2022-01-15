package com.kappstudio.joboardgame.ui.party

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.databinding.ItemPhotoBinding
import com.kappstudio.joboardgame.ui.partydetail.PartyDetailViewModel
import timber.log.Timber

class PhotoAdapter(private val viewModel: PartyDetailViewModel) :
    ListAdapter<String, PhotoAdapter.PhotoViewHolder>(DiffCallback) {

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String, viewModel: PartyDetailViewModel) {
            Timber.d("bind")
            bindImage(binding.ivPhoto, url)
//            binding.ivPhoto.setOnClickListener {
//                viewModel.navToFullPhoto(url)
//            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        Timber.d("create")
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        Timber.d("BindViewHolder")

        holder.bind(getItem(position), viewModel)
    }
}