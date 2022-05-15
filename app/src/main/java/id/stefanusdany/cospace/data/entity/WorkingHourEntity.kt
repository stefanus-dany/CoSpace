package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkingHourEntity(
    val id: Int = 0,
    val day: String = "",
    val startHour: String = "",
    val endHour: String = ""
) : Parcelable
