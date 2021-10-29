package com.kappstudio.jotabletopgame.tools.dice

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.databinding.ItemDiceBinding

class DiceAdapter(var viewModel: DiceViewModel) :
    ListAdapter<Int, DiceAdapter.DiceViewHolder>(DiffCallback) {

    class DiceViewHolder(private val binding: ItemDiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int, viewModel: DiceViewModel) {

            binding.lottieDice.setAnimation(R.raw.anim_dice)
            if (viewModel.isRolling.value == true) {
                binding.lottieDice.frame=0

                binding.ivDice.visibility = View.INVISIBLE
                binding.lottieDice.visibility = View.VISIBLE
                binding.lottieDice.speed = 0.8F
                binding.lottieDice.setAnimation(R.raw.anim_dice)
                binding.lottieDice.playAnimation()
                Handler().postDelayed({
                    binding.ivDice.setImageResource(viewModel.getRollResult())
                    binding.ivDice.visibility = View.VISIBLE
                    binding.lottieDice.visibility = View.GONE

                }, 700)

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