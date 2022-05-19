package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatEntity(
    var id: String = "",
    var name: String = "",
    var photoProfile: String = "",
    var text: String = "",
    var timeDate: String = ""
) : Parcelable
