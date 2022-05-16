package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostEntity(
    val id: String = "",
    val title: String = "",
    val desc: String = "",
    val image: String = "",
) : Parcelable
