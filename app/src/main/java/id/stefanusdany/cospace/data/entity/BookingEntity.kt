package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingEntity(
    val id: String = "",
    val capacity: Int = 0,
    val date: String = "",
    val paymentSlip: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val startHour: String = "",
    val endHour: String = "",
    val timeDuration: String = "",
    val totalPrice: Int = 0
) : Parcelable
