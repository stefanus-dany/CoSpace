package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TmpEntity(
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var capacity: Int = 0,
    var googleMaps: String = "",
    var lat: Float = 0F,
    var long: Float = 0F,
    var price: Int = 0,
    var image: String = ""
) : Parcelable