package id.stefanusdany.cospace.ui.adminCoS.adminChat

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.ChatEntity

class AdminChatViewModel(private val repository: Repository) : ViewModel() {

    fun getAllChatsCoSpace(idCoSpace: String) = repository.getAllChatsCoSpace(idCoSpace)

    fun getAllDetailChat(idChat: String) = repository.getAllDetailChat(idChat)

    fun sendChatToDatabase(data: ChatEntity, idDetailChat: String) = repository.sendChatToDatabase(data, idDetailChat)

}