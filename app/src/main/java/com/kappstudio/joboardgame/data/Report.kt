package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Report(
    var id: String = "",
    var reportTime: Long = Calendar.getInstance().timeInMillis,
    var thing: String = "",
    var violationId: String = ""
): Parcelable
