package id.stefanusdany.cospace.ui.adminCoS.facilityCoS

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.FacilityEntity

class FacilityViewModel(private val repository: Repository) : ViewModel() {

    fun getAllFacility(idCoSpace: String) = repository.getAllFacility(idCoSpace)

    fun deleteFacility(idCoSpace: String, idFacility: String) = repository.deleteFacility(idCoSpace, idFacility)

    fun addFacility(idCoSpace: String, facility: FacilityEntity) = repository.addFacility(idCoSpace, facility)

}