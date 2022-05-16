package id.stefanusdany.cospace.ui.user.payment

import android.net.Uri
import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.BookingEntity

class PaymentViewModel(private val repository: Repository) : ViewModel() {

    fun uploadBooking(dataBooking: BookingEntity, idCoS: String, bookingId: String) =
        repository.uploadBooking(dataBooking, idCoS, bookingId)

    fun uploadPaymentSlip(
        uriPaymentSlip: Uri?, idCoS: String, bookingId: String
    ) = repository.uploadPaymentSlip(uriPaymentSlip, idCoS, bookingId)
}