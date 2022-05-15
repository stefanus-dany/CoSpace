package id.stefanusdany.cospace.ui.user.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.data.entity.PostEntity
import id.stefanusdany.cospace.databinding.ItemPostedBinding
import id.stefanusdany.cospace.helper.Helper.loadImage

class DetailAdapter(private val onItemClick: ((PostEntity) -> Unit)) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    private var data = ArrayList<PostEntity>()

    fun setData(data: List<PostEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemPostedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemPostedBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PostEntity) {
            with(binding) {
                ivPost.loadImage(data.image)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(data[adapterPosition])
            }
        }
    }
}