package com.kappstudio.joboardgame.tools.dice

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.ItemDiceBinding

class DiceAdapter(var viewModel: DiceViewModel) :
    ListAdapter<Int, DiceAdapter.DiceViewHolder>(DiffCallback) {

    class DiceViewHolder(private val binding: ItemDiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int, viewModel: DiceViewModel) {
            binding.lottieDice.frame = 0
            binding.lottieDice.speed = 1.0F

            binding.lottieDice.setAnimation(R.raw.anim_dice)
            if (viewModel.isRolling.value == true) {

                binding.lottieDice.playAnimation()
                Handler().postDelayed({
                    binding.lottieDice.pauseAnimation()
                    binding.lottieDice.frame = viewModel.getRollResult()

                }, 800)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceViewHolder {
        return DiceViewHolder(
            ItemDiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DiceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}