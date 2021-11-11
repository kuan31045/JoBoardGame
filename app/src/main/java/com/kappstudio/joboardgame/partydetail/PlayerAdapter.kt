package com.kappstudio.joboardgame.partydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.databinding.ItemUserBinding
import timber.log.Timber

class PlayerAdapter(private val viewModel: PartyDetailViewModel) :
    ListAdapter<User, PlayerAdapter.PlayerViewHolder>(DiffCallback) {
init {
    Timber.d(" init")

}
   inner class PlayerViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, viewModel: PartyDetailViewModel, position: Int) {
            if(position%2==0){
                binding.clUser.marginTop.plus(100)

            }
            Timber.d("bind")
            bindImage(binding.ivUser,user.image)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        Timber.d("create")
        return PlayerViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        Timber.d("BindViewHolde")

        holder.bind(getItem(position),viewModel, position)
    }
}