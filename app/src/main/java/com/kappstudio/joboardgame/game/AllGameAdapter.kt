package com.kappstudio.joboardgame.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.databinding.ItemGameAllBinding
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlin.math.roundToInt

class AllGameAdapter(private val viewModel: GameViewModel) :
    ListAdapter<Game, AllGameAdapter.GameViewHolder>(DiffCallback) {

    class GameViewHolder(private var binding: ItemGameAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: ViewModel) {
            binding.apply {
                tvName.text = game.name
                tvTime.text = game.time.toString()
                tvPlayerQty.text = "${game.minPlayerQty}-${game.maxPlayerQty}"
val avg = game.totalRating / game.ratingQty.toFloat()

                tvRating.text = ((avg * 10.0).roundToInt() / 10.0).toString()
                bindImage(ivGame, game.image)

                clGame.setOnClickListener {
                    when (viewModel) {
                        is NavToGameDetailInterface -> viewModel.navToGameDetail(game)
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
