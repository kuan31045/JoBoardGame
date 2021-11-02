package com.kappstudio.jotabletopgame.newparty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.databinding.ItemGameAddBinding
import timber.log.Timber

class AddGameAdapter(private val viewModel: NewPartyViewModel) :
    ListAdapter<Game, AddGameAdapter.GameViewHolder>(DiffCallback) {

    inner class GameViewHolder(private val binding: ItemGameAddBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(game: Game, viewModel: NewPartyViewModel) {

            binding.apply {
                tvName.text = game.name
                bindImage(ivGame, game.image)

                tvRemove.setOnClickListener {
                    viewModel.removeGame(game)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        Timber.d("create")
        return GameViewHolder(
            ItemGameAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        Timber.d("BindViewHolder")

        holder.bind(getItem(position),viewModel)
    }
}