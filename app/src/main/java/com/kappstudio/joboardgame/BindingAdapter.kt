package com.kappstudio.joboardgame

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kappstudio.joboardgame.album.AlbumAdapter
import com.kappstudio.joboardgame.party.PhotoAdapter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
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
    val formatter = SimpleDateFormat("yyyy年MM月dd日 hh:mm")
    tv.text = formatter.format(time)

}

@BindingAdapter("cProgress")
fun bindCircularProgressBar(cp: CircularProgressBar, cProgress: Float) {
    cp.progress = cProgress
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
    photos?.let {


        rv.adapter?.apply {
            when (this) {
                is PhotoAdapter -> submitList(photos.sortedByDescending { it })
                is AlbumAdapter -> submitList(photos.sortedByDescending { it })

            }
        }
    }
}


@BindingAdapter("rvHeight")
fun bindRecyclerViewHeight(rv: RecyclerView, rvHeight: Int) {
    // 高度
    rv.layoutParams.height = rvHeight
    // 禁滾
    rv.layoutManager = object : LinearLayoutManager(appInstance) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}