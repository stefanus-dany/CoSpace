package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoWorkingSpaceEntity(
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var capacity: Int = 0,
    var googleMaps: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var price: Int = 0,
    var image: String = "",
    var rating: Double = 0.0,
    var post: List<PostEntity> = listOf(),
    var workingHour: List<WorkingHourEntity> = listOf(),
    var images: List<ImagesEntity> = listOf(),
    var booking: List<BookingEntity> = listOf(),
    var facility: List<FacilityEntity> = listOf()
) : Parcelable