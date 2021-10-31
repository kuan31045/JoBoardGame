package com.kappstudio.jotabletopgame.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.databinding.ItemGameFavoriteBinding
import timber.log.Timber

class FavoriteAdapter(private val viewModel: FavoriteViewModel) :
    ListAdapter<Game, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    inner class FavoriteViewHolder(private val binding: ItemGameFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(game: Game, viewModel: FavoriteViewModel) {

            binding.apply {
                tvName.text = game.name
                bindImage(ivGame, game.image)

                tvRemove.setOnClickListener {
                    viewModel.removeFavorite(game)
                }

                clGame.setOnClickListener {
                        viewModel.navToGameDetail(game.id)
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
        Timber.d("create")
        return FavoriteViewHolder(
            ItemGameFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        Timber.d("BindViewHolder")

        holder.bind(getItem(position),viewModel)
    }
}