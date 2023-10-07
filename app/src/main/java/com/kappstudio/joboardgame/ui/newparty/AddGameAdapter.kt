package com.kappstudio.joboardgame.ui.newparty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.databinding.ItemGameAddBinding

class AddGameAdapter(private val viewModel: NewPartyViewModel) :
    ListAdapter<Game, AddGameAdapter.GameViewHolder>(DiffCallback) {

    inner class GameViewHolder(private val binding: ItemGameAddBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: NewPartyViewModel) {
            binding.apply {
                tvName.text = game.name
                bindImage(ivGame, game.image)

                tvRemove.setOnClickListener {
                    viewModel.removeGame(game.name)
                }

                if (game.time != 0) {
                    tvTime.text = game.time.toString()
                    tvPlayerQty.text = "${game.minPlayerQty}-${game.maxPlayerQty}"
                    tvNoData.visibility = View.GONE

                } else {
                    tvNoData.visibility = View.VISIBLE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}