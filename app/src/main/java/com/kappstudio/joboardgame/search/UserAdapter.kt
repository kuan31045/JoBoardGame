package com.kappstudio.joboardgame.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.databinding.ItemUserBinding
import com.kappstudio.joboardgame.partydetail.PlayerAdapter
import timber.log.Timber

class UserAdapter(private val viewModel: SearchViewModel) :
    ListAdapter<User, UserAdapter.UserViewHolder>(UserAdapter) {


    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, viewModel: SearchViewModel) {

            bindImage(binding.ivUser, user.image)
            binding.tvName.text = user.name
            binding.clUser.setOnClickListener {
                viewModel.navToUser(user.id)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}