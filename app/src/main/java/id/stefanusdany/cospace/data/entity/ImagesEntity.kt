package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImagesEntity(
    var id: String = "",
    var url: String = ""
) : Parcelable
