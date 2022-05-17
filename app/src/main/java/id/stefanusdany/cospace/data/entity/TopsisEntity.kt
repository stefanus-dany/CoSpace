package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopsisEntity(
    var id: String = "",
    var name: String = "",
    var distance: Double = 0.0,
    var price: Int = 0,
    var rating: Double = 0.0,
) : Parcelable
