package id.stefanusdany.cospace.ui.user.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository

class ResultViewModel(private val repository: Repository) : ViewModel() {

    fun getAllCoWorkingSpace() = repository.getAllCoWorkingSpace()
}