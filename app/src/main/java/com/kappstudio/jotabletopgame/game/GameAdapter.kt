package com.kappstudio.jotabletopgame.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.databinding.ItemGameInfoBinding
import com.kappstudio.jotabletopgame.databinding.ItemGameSimpleBinding

class GameAdapter(val type: GameAdapterType, val isHorizontal: Boolean = true) :
    ListAdapter<Game, RecyclerView.ViewHolder>(DiffCallback) {

    class InfoViewHolder(private var binding: ItemGameInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, isHorizontal: Boolean) {
            binding.apply {
                tvName.text = game.name
                tvTime.text = game.time.toString()
                tvPlayerQty.text = "${game.minPlayerQty} - ${game.maxPlayerQty}"
                tvRating.text =game.avgRating.toString()

                if (isHorizontal) {
                    ivGame.layoutParams.width =
                        appInstance.resources.getDimensionPixelSize(R.dimen.game_item_image)
                }
            }
        }
    }

    class SimpleViewHolder(private val binding: ItemGameSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, isHorizontal: Boolean) {
            binding.apply {
                tvName.text = game.name
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (type) {
            GameAdapterType.SIMPLE -> {
                SimpleViewHolder(
                    ItemGameSimpleBinding.inflate(LayoutInflater.from(parent.context))
                )
            }

            GameAdapterType.INFO -> {
                InfoViewHolder(
                    ItemGameInfoBinding.inflate(LayoutInflater.from(parent.context))
                )
            }
            else -> {
                SimpleViewHolder(
                    ItemGameSimpleBinding.inflate(LayoutInflater.from(parent.context))
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is SimpleViewHolder -> {
                holder.bind(getItem(position), isHorizontal)
            }
            is InfoViewHolder -> {
                holder.bind(getItem(position), isHorizontal)
            }
        }
    }
}
