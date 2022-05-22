package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginEntity(
    var id: String = "",
    var name: String = "",
    var password: String = "",
    var username: String = "",
    var photoProfile: String = ""
) : Parcelable
