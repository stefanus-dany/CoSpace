package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImagesEntity(
    val id: String = "",
    val url: String = ""
) : Parcelable
