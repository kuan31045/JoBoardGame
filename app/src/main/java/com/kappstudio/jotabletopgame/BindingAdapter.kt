package com.kappstudio.jotabletopgame

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kappstudio.jotabletopgame.data.Game

@BindingAdapter("imageUrl")
fun bindImage(iv: ImageView, imgUrl: String) {
    Glide.with(iv.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.image_defult)
                .error(R.drawable.image_defult)
        )
        .into(iv)
}

