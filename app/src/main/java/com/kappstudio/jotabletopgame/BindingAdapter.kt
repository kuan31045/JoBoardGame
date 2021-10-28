package com.kappstudio.jotabletopgame

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

@BindingAdapter("countries")
fun bindSpinnerCountries(spn: Spinner, countries: Boolean = true) {
    spn.adapter = ArrayAdapter(
        appInstance,
        android.R.layout.simple_spinner_dropdown_item,
        appInstance.resources.getStringArray(R.array.country_list).toList()
    )
}