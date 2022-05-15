package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoWorkingSpaceEntity(
    var id: Int = 0,
    var name: String = "",
    var address: String = "",
    var capacity: Int = 0,
    var googleMaps: String = "",
    var lat: Float = 0F,
    var long: Float = 0F,
    var price: Int = 0,
    var post: List<PostEntity> = listOf(),
    var workingHour: List<WorkingHourEntity> = listOf(),
    var images: List<String> = listOf(),
    var booking: List<BookingEntity> = listOf(),
    var facility: List<FacilityEntity> = listOf()
) : Parcelable