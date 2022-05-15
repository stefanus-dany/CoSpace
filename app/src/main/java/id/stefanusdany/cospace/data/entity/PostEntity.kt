package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostEntity(
    val id: Int = 0,
    val title: String = "",
    val image: String = "",
) : Parcelable
