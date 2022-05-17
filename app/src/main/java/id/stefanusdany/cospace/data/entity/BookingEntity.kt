package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingEntity(
    var id: String = "",
    var capacity: Int = 0,
    var date: String = "",
    var paymentSlip: String = "",
    var phoneNumber: String = "",
    var name: String = "",
    var startHour: String = "",
    var endHour: String = "",
    var timeDuration: String = "",
    var totalPrice: Int = 0
) : Parcelable
