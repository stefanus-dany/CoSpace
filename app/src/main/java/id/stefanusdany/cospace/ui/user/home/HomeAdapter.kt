package id.stefanusdany.cospace.ui.user.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.databinding.ItemHomeBinding
import id.stefanusdany.cospace.helper.Helper.loadImage

class HomeAdapter(private val onItemClick: ((CoWorkingSpaceEntity) -> Unit)) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var data = ArrayList<CoWorkingSpaceEntity>()

    fun setData(data: List<CoWorkingSpaceEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemHomeBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CoWorkingSpaceEntity) {
            with(binding) {
                tvName.text = data.name
                civProfileImage.loadImage(data.image)

                var facility = ""

                for (i in 0 until data.facility.size) {
                    facility += if (i == data.facility.size - 1) {
                        data.facility[i].name
                    } else {
                        data.facility[i].name + ", "
                    }
                }
                tvFacility.text = facility
                tvCapacity.text =
                    itemView.resources.getString(R.string.format_capacity, data.capacity.toString())
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(data[adapterPosition])
            }
        }
    }
}