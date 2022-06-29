package id.stefanusdany.cospace.ui.user.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.data.entity.WorkingHourEntity

class ResultViewModel(private val repository: Repository) : ViewModel() {

    fun getAllCoWorkingSpace() = repository.getAllCoWorkingSpace()

    fun getAllBooking(idCoSpace: String) = repository.getAllSuccessfulBooking(idCoSpace)

    fun getAllFacility(idCoSpace: String) = repository.getAllFacility(idCoSpace)

    fun getAllWorkingHour(idCoSpace: String) = repository.getAllWorkingHour(idCoSpace)

    val getAllDataCoworkingSpace: LiveData<List<CoWorkingSpaceEntity>> =
        object : MediatorLiveData<List<CoWorkingSpaceEntity>>() {
            var listCoWorkingSpaceEntity: List<CoWorkingSpaceEntity>? = null
            var listFacilityEntity: List<FacilityEntity>? = null
            var listWorkingHOurEntity: List<WorkingHourEntity>? = null
            var listBookingEntity: List<BookingEntity>? = null

            init {
                addSource(getAllCoWorkingSpace()) { listCoWorkingSpaceEntity ->
                    this.listCoWorkingSpaceEntity = listCoWorkingSpaceEntity
                    value = listCoWorkingSpaceEntity
                    if (!listCoWorkingSpaceEntity.isNullOrEmpty()) {
                        for (i in listCoWorkingSpaceEntity.indices) {
                            addSource(getAllFacility(idCoSpace = listCoWorkingSpaceEntity[i].id)) { listFacilityEntity ->
                                this.listFacilityEntity = listFacilityEntity
                                listCoWorkingSpaceEntity[i].facility = listFacilityEntity
                                value = listCoWorkingSpaceEntity
                            }
                            addSource(getAllWorkingHour(idCoSpace = listCoWorkingSpaceEntity[i].id)) { listWorkingHOurEntity ->
                                this.listWorkingHOurEntity = listWorkingHOurEntity
                                listCoWorkingSpaceEntity[i].workingHour = listWorkingHOurEntity
                                value = listCoWorkingSpaceEntity
                            }
                            addSource(getAllBooking(idCoSpace = listCoWorkingSpaceEntity[i].id)) { listBookingEntity ->
                                this.listBookingEntity = listBookingEntity
                                listCoWorkingSpaceEntity[i].booking = listBookingEntity
                                value = listCoWorkingSpaceEntity
                            }
                        }
                    }
                }

            }
        }
}