package id.stefanusdany.cospace.ui.adminCoS.adminChat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.data.entity.IdChatEntity
import id.stefanusdany.cospace.databinding.ItemChatBinding
import id.stefanusdany.cospace.helper.Helper.loadImage

class AdminChatAdapter(private val onItemClick: ((IdChatEntity) -> Unit)) :
    RecyclerView.Adapter<AdminChatAdapter.ViewHolder>() {
    private var data = ArrayList<IdChatEntity>()

    fun setData(data: List<IdChatEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemChatBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: IdChatEntity) {
            with(binding) {
                tvName.text = data.name
                civProfileImage.loadImage(data.photoProfile)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(data[adapterPosition])
            }
        }
    }
}