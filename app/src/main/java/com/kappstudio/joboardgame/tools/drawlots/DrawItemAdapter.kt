package com.kappstudio.joboardgame.tools.drawlots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.databinding.ItemDrawBinding

class DrawItemAdapter(var viewModel: DrawLotsViewModel) :
    ListAdapter<String, DrawItemAdapter.DiceViewHolder>(DiffCallback) {

    inner class DiceViewHolder(private val binding: ItemDrawBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, item: String, viewModel: DrawLotsViewModel) {
            binding.tvPosition.text = (position + 1).toString()+"."
            binding.tvItem.text = item

            binding.ivRemove.setOnClickListener {
                viewModel.removeItem(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceViewHolder {
        return DiceViewHolder(
            ItemDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DiceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(position, item, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}