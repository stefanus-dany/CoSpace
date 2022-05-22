package id.stefanusdany.cospace.ui.user.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.data.entity.ChatEntity
import id.stefanusdany.cospace.databinding.ItemDetailChatBinding
import id.stefanusdany.cospace.helper.Helper.loadImage

class AdminDetailChatAdapter(private val onItemClick: ((ChatEntity) -> Unit)) :
    RecyclerView.Adapter<AdminDetailChatAdapter.ViewHolder>() {
    private var data = ArrayList<ChatEntity>()

    fun setData(data: List<ChatEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemDetailChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemDetailChatBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatEntity) {
            with(binding) {
                tvContent.text = data.text
                tvName.text = data.name
                civ.loadImage(data.photoProfile)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(data[adapterPosition])
            }
        }
    }
}