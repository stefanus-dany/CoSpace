package id.stefanusdany.cospace.ui.adminCoS.detailCoS

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.WorkingHourEntity

class CoSDetailViewModel(private val repository: Repository) : ViewModel() {

    fun getAllWorkingHour(idCoSpace: String) = repository.getAllWorkingHour(idCoSpace)

    fun addWorkingHour(idCoSpace: String, workingHour: WorkingHourEntity) =
        repository.addWorkingHour(idCoSpace, workingHour)

    fun deleteWorkingHour(idCoSpace: String, workingHour: WorkingHourEntity) =
        repository.deleteWorkingHour(idCoSpace, workingHour)

    fun getCoWorkingSpaceDetail(idCoSpace: String) = repository.getCoWorkingSpaceDetail(idCoSpace)

    fun saveCoWorkingSpaceDetail(idCoSpace: String, data: List<Any>) =
        repository.saveCoWorkingSpaceDetail(idCoSpace, data)

}