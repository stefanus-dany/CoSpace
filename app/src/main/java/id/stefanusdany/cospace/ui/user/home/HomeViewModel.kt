package id.stefanusdany.cospace.ui.user.home

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getAllCoWorkingSpace() = repository.getAllCoWorkingSpace()
}