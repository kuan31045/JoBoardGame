package com.kappstudio.joboardgame.ui.newparty.selectedgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.databinding.ItemGameSelectedBinding

class SelectGameAdapter(private val viewModel: SelectGameViewModel) :
    ListAdapter<Game, SelectGameAdapter.GameViewHolder>(DiffCallback) {

    class GameViewHolder(private var binding: ItemGameSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: SelectGameViewModel) {
            binding.viewModel = viewModel
            binding.game = game
            binding.apply {
                tvName.text = game.name
                tvTime.text = game.time.toString()
                tvPlayerQty.text = "${game.minPlayerQty}-${game.maxPlayerQty}"
                bindImage(ivGame, game.image)
                clGame.setOnClickListener {
                    viewModel.selectGame(game)


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
            ItemGameSelectedBinding.inflate(
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
