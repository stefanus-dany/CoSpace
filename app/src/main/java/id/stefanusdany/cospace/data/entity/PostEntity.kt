package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostEntity(
    var id: String = "",
    var title: String = "",
    var desc: String = "",
    var image: String = "",
) : Parcelable
