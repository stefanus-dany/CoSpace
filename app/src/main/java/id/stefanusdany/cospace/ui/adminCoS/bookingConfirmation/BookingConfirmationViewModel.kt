package id.stefanusdany.cospace.ui.adminCoS.bookingConfirmation

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.BookingEntity

class BookingConfirmationViewModel(private val repository: Repository) : ViewModel() {

    fun getAllBookingConfirmation(idCoSpace: String) =
        repository.getAllBookingConfirmation(idCoSpace)

    fun sendAcceptedBooking(idCoSpace: String, bookingData: BookingEntity) =
        repository.sendAcceptedBooking(idCoSpace, bookingData)

    fun deleteAcceptedBooking(idCoSpace: String, bookingData: BookingEntity) =
        repository.deleteAcceptedBooking(idCoSpace, bookingData)
}