package id.stefanusdany.cospace.ui.adminCoS.login

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.ChatEntity
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun getAuthentication(username: String, password: String) = repository.getAuthentication(username, password)

}