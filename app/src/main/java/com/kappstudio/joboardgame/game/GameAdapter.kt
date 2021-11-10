package com.kappstudio.joboardgame.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.databinding.ItemGameSimpleBinding
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface

class GameAdapter(private val viewModel: ViewModel) :
    ListAdapter<Game, RecyclerView.ViewHolder>(DiffCallback) {


    class SimpleViewHolder(private val binding: ItemGameSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, viewModel: ViewModel) {

            binding.apply {
                tvName.text = game.name
                bindImage(ivGame, game.image)

                clGame.setOnClickListener {
                    when(viewModel){
                        is NavToGameDetailInterface -> viewModel.navToGameDetail(game)
                    }
                }

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

        return when (viewModel) {

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
                holder.bind(getItem(position), viewModel)

            }

        }
    }

}
