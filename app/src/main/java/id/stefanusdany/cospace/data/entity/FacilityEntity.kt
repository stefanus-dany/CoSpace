package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FacilityEntity(
    val id: String = "",
    val name: String = ""
) : Parcelable
