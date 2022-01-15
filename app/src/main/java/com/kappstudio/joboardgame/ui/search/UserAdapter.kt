package com.kappstudio.joboardgame.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.databinding.ItemUserBinding
import com.kappstudio.joboardgame.ui.user.NavToUserInterface

class UserAdapter(private val viewModel: ViewModel) :
    ListAdapter<User, UserAdapter.UserViewHolder>(UserAdapter) {


    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, viewModel: ViewModel) {

            bindImage(binding.ivUser, user.image)
            binding.tvName.text = user.name
            binding.clUser.setOnClickListener {
                when (viewModel) {
                    is NavToUserInterface -> viewModel.navToUser(user.id)
                }
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