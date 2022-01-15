package com.kappstudio.joboardgame.ui.newgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.databinding.ItemCheckBoxBinding

class ToolAdapter(private val viewModel: NewGameViewModel) :
    ListAdapter<String, ToolAdapter.ToolViewHolder>(ToolAdapter) {


    inner class ToolViewHolder(private val binding: ItemCheckBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: String, viewModel: NewGameViewModel) {
            binding.cb.text = tool
            binding.cb.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> viewModel.addTool(tool)

                    false -> viewModel.removeTool(tool)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        return ToolViewHolder(
            ItemCheckBoxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}