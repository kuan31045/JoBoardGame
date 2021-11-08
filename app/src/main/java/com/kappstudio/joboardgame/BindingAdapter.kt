package com.kappstudio.joboardgame

import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kappstudio.joboardgame.album.AlbumAdapter
import com.kappstudio.joboardgame.party.PhotoAdapter
import java.text.SimpleDateFormat
import java.util.*

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
    val formatter = SimpleDateFormat("yyyy.MM.dd hh:mm")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    tv.text = formatter.format(calendar.time)

}

@BindingAdapter("imgUrl")
fun bindImage(iv: ImageView, imgUrl: String?) {

    Glide.with(iv.context)
        .load(imgUrl)

        .apply(
            RequestOptions()
                .placeholder(R.drawable.image_defult)
                .error(R.drawable.image_defult)

        )

        .into(iv)

}


@BindingAdapter("photos")
fun bindRecyclerViewPhotos(rv: RecyclerView, photos: List<String>?) {
    photos?.let{


    rv.adapter?.apply {
        when (this) {
            is PhotoAdapter -> submitList(photos.sortedByDescending { it })
            is AlbumAdapter -> submitList(photos.sortedByDescending { it })

        }
    }}
}


