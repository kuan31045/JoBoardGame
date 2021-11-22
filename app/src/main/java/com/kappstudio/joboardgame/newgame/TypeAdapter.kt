package com.kappstudio.joboardgame.newgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.databinding.ItemCheckBoxBinding

class TypeAdapter(private val viewModel: NewGameViewModel) :
    ListAdapter<String, TypeAdapter.TypeViewHolder>(TypeAdapter) {


    inner class TypeViewHolder(private val binding: ItemCheckBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(type: String, viewModel: NewGameViewModel) {
            binding.cb.text = type

            binding.cb.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> viewModel.addType(type)

                    false -> viewModel.removeType(type)
                }
            }

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        return TypeViewHolder(
            ItemCheckBoxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}