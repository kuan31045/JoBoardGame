package com.kappstudio.joboardgame.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.databinding.ItemCheckBoxBinding
import com.kappstudio.joboardgame.game.GameViewModel

class FilterAdapter(private val viewModel: GameViewModel) :
    ListAdapter<String, FilterAdapter.FilterViewHolder>(FilterAdapter) {


    inner class FilterViewHolder(private val binding: ItemCheckBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(type: String, viewModel: GameViewModel) {
binding.cb.text=type
            binding.cb.setOnClickListener {
                viewModel.addFilter(type)
            }


        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ItemCheckBoxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}