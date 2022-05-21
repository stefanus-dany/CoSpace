package id.stefanusdany.cospace.ui.adminCoS.successfulBooking

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.BookingEntity

class SuccessfulBookingViewModel(private val repository: Repository) : ViewModel() {

    fun getAllBookingConfirmation(idCoSpace: String) =
        repository.getAllBookingConfirmation(idCoSpace)

    fun sendAcceptedBooking(idCoSpace: String, bookingData: BookingEntity) =
        repository.sendAcceptedBooking(idCoSpace, bookingData)

    fun getAllSuccessfulBooking(idCoSpace: String) =
        repository.getAllSuccessfulBooking(idCoSpace)
}