package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdChatEntity(
    var id: String = "",
    var idChat: String = "",
    var name: String = "",
    var photoProfile: String = ""
) : Parcelable
