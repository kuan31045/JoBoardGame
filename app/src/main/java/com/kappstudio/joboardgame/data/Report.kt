package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

@Parcelize
data class Report(
    val id: String = "",
    val reportTime: Long = Calendar.getInstance().timeInMillis,
    val thing: String = "",
    val violationId: String = ""
): Parcelable