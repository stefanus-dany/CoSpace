package id.stefanusdany.cospace.ui.chat

import androidx.lifecycle.ViewModel
import id.stefanusdany.cospace.data.Repository
import id.stefanusdany.cospace.data.entity.ChatEntity
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity

class ChatViewModel(private val repository: Repository) : ViewModel() {

    fun getAllChatsUser(uuid: String) = repository.getAllChatsUser(uuid)

    fun getAllDetailChat(idChat: String) = repository.getAllDetailChat(idChat)

    fun sendChatToDatabase(data: ChatEntity, idDetailChat: String) = repository.sendChatToDatabase(data, idDetailChat)

}