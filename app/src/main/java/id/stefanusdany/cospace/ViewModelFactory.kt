package id.stefanusdany.cospace

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.di.Injection
import id.stefanusdany.cospace.ui.adminCoS.bookingConfirmation.BookingConfirmationViewModel
import id.stefanusdany.cospace.ui.adminCoS.detailCoS.CoSDetailViewModel
import id.stefanusdany.cospace.ui.adminCoS.facilityCoS.FacilityViewModel
import id.stefanusdany.cospace.ui.adminCoS.login.LoginViewModel
import id.stefanusdany.cospace.ui.adminCoS.successfulBooking.SuccessfulBookingViewModel
import id.stefanusdany.cospace.ui.adminCoS.adminChat.AdminChatViewModel
import id.stefanusdany.cospace.ui.user.chat.ChatViewModel
import id.stefanusdany.cospace.ui.user.detail.DetailViewModel
import id.stefanusdany.cospace.ui.user.home.HomeViewModel
import id.stefanusdany.cospace.ui.user.payment.PaymentViewModel
import id.stefanusdany.cospace.ui.user.recommendation.RecommendationViewModel
import id.stefanusdany.cospace.ui.user.recommendation.ResultViewModel

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> {
                PaymentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RecommendationViewModel::class.java) -> {
                RecommendationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AdminChatViewModel::class.java) -> {
                AdminChatViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookingConfirmationViewModel::class.java) -> {
                BookingConfirmationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SuccessfulBookingViewModel::class.java) -> {
                SuccessfulBookingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FacilityViewModel::class.java) -> {
                FacilityViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CoSDetailViewModel::class.java) -> {
                CoSDetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}