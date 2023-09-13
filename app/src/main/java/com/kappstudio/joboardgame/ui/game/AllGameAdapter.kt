package com.kappstudio.joboardgame.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.databinding.ItemGameAllBinding
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import kotlin.math.roundToInt

class AllGameAdapter(private val viewModel: ViewModel) :
    ListAdapter<Game, AllGameAdapter.GameViewHolder>(DiffCallback) {

    class GameViewHolder(private var binding: ItemGameAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: ViewModel) {
            binding.apply {
                tvName.text = game.name
                tvTime.text = game.time.toString()
                tvPlayerQty.text = "${game.minPlayerQty}-${game.maxPlayerQty}"
                val avg = game.totalRating / game.ratingQty.toFloat()

                if (game.ratingQty > 0) {
                    tvRating.text = ((avg * 10.0).roundToInt() / 10.0).toString()
                    tvRating.visibility = View.VISIBLE
                } else {
                    tvRating.visibility = View.GONE
                }

                bindImage(ivGame, game.image)

                game.type.forEach {
                    tvType.text = "${tvType.text}$it"
                    if (it != game.type.last()) {
                        tvType.text = "${tvType.text} | "

                    }
                }

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