package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkingHourEntity(
    var id: String = "",
    var day: String = "",
    var startHour: String = "",
    var endHour: String = ""
) : Parcelable
