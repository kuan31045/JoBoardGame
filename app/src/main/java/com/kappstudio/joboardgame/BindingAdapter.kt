package com.kappstudio.joboardgame

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kappstudio.joboardgame.ui.album.AlbumAdapter
import com.kappstudio.joboardgame.ui.party.PhotoAdapter
import java.text.SimpleDateFormat

@BindingAdapter("toTools")
fun bindToToolsBtn(btn: Button, tools: List<String>?) {
    if (tools != null) {
        btn.visibility = when (tools.contains(btn.text)) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }
}

@BindingAdapter("gameTypes")
fun bindTextViewGameTypes(tv: TextView, gameTypes: List<String>) {
    var str = ""
    gameTypes.forEach {
        str += "$it"
        if (it != gameTypes.last()) {
            str += " | "
        }
    }
    tv.text = str
}

@BindingAdapter("date")
fun bindTextViewDate(tv: TextView, time: Long) {
    val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
    tv.text = formatter.format(time)
}

@BindingAdapter("imgUrl")
fun bindImage(iv: ImageView, imgUrl: String?) {
    Glide.with(iv.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.image_defult)
                .error(R.drawable.image_defult)
        ).into(iv)
}

@BindingAdapter("photos")
fun bindRecyclerViewPhotos(rv: RecyclerView, photos: List<String>?) {
    photos?.let {
        rv.adapter?.apply {
            when (this) {
                is PhotoAdapter -> submitList(photos.sortedByDescending { it })
                is AlbumAdapter -> submitList(photos.sortedByDescending { it })
            }
        }
    }
}

fun bindNotFoundLottie(lottie: LottieAnimationView, tv: TextView, list: List<Any>) {
    when (list.size) {
        0 -> {
            lottie.visibility = View.VISIBLE
            tv.visibility = View.VISIBLE
        }
        else -> {
            lottie.visibility = View.GONE
            tv.visibility = View.GONE
        }
    }
}


