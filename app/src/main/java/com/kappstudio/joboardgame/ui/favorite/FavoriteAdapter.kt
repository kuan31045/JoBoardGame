package com.kappstudio.joboardgame.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.databinding.ItemGameFavoriteBinding

class FavoriteAdapter(private val viewModel: FavoriteViewModel, private val canEdit: Boolean) :
    ListAdapter<Game, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    inner class FavoriteViewHolder(private val binding: ItemGameFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: FavoriteViewModel) {

            binding.apply {
                tvName.text = game.name
                bindImage(ivGame, game.image)

                if (canEdit) {
                    tvRemove.visibility = View.VISIBLE
                } else {
                    tvRemove.visibility = View.GONE
                }
                tvRemove.setOnClickListener {
                    viewModel.removeFavorite(game)
                }
                clGame.setOnClickListener {
                    viewModel.navToGameDetail(game)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemGameFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}