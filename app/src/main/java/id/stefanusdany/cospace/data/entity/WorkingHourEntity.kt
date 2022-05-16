package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkingHourEntity(
    val id: String = "",
    val day: String = "",
    val startHour: String = "",
    val endHour: String = ""
) : Parcelable
