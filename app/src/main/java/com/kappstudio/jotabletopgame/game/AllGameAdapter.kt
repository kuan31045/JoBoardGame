package com.kappstudio.jotabletopgame.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.bindTextViewGameTypes
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.databinding.ItemGameAllBinding
import com.kappstudio.jotabletopgame.gamedetail.NavToGameDetailInterface

class AllGameAdapter(private val viewModel: GameViewModel) :
    ListAdapter<Game, AllGameAdapter.GameViewHolder>(DiffCallback) {

    class GameViewHolder(private var binding: ItemGameAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: ViewModel) {
            binding.apply {
                tvName.text = game.name
                tvTime.text = game.time.toString()
                tvPlayerQty.text = "${game.minPlayerQty}-${game.maxPlayerQty}"
                tvRating.text = game.avgRating.toString()
                bindImage(ivGame, game.image)

                clGame.setOnClickListener {
                    when (viewModel) {
                        is NavToGameDetailInterface -> viewModel.navToGameDetail(game.id)
                    }
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameAllBinding.inflate(
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
