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
    var rating: Double = 0.0,
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var price: Int = 0,
    var image: String = ""
) : Parcelable