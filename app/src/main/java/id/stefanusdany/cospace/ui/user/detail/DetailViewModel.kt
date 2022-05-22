package id.stefanusdany.cospace.ui.user.detail

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.IdChatEntity

class DetailViewModel(private val repository: Repository) : ViewModel() {

    fun creatingChats(
        idChatEntityUser: IdChatEntity,
        idChatEntityCoSpace: IdChatEntity
    ) = repository.creatingChats(idChatEntityUser, idChatEntityCoSpace)

    fun isExistChatWithCoSpace(idUser: String, idCoSpace: String) = repository.isExistChatWithCoSpace(idUser, idCoSpace)
}