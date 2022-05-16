package id.stefanusdany.cospace.ui.user.home

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getAllCoWorkingSpace() = repository.getAllCoWorkingSpace()
}