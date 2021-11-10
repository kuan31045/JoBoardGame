package com.kappstudio.joboardgame.myrating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.databinding.ItemGameRatingBinding
import timber.log.Timber

class RatingAdapter(private val viewModel: MyRatingViewModel) :
    ListAdapter<Rating, RatingAdapter.RatingViewHolder>(DiffCallback) {

    inner class RatingViewHolder(private val binding: ItemGameRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(rating: Rating, viewModel: MyRatingViewModel) {

            binding.apply {
                tvName.text = rating.game.name
                tvRating.text = rating.score.toString()
                bindImage(ivGame, rating.game.image)

                tvRating.setOnClickListener {
                    viewModel.navToRating(rating)
                 }

                clGame.setOnClickListener {
                    viewModel.navToGameDetail(rating.game)
                }


            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem.game.name == newItem.game.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        Timber.d("create")
        return RatingViewHolder(
            ItemGameRatingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        Timber.d("BindViewHolder")

        holder.bind(getItem(position),viewModel)
    }
}