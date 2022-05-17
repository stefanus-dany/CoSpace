package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FacilityEntity(
    var id: String = "",
    var name: String = ""
) : Parcelable
