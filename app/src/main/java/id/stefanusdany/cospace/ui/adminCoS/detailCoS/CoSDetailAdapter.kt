package id.stefanusdany.cospace.ui.adminCoS.detailCoS

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.WorkingHourEntity
import id.stefanusdany.cospace.databinding.ItemCosDetailBinding
import id.stefanusdany.cospace.helper.Helper

class CoSDetailAdapter(private val cosDetailAction: CoSDetailAction) :
    RecyclerView.Adapter<CoSDetailAdapter.ViewHolder>() {
    private var data = ArrayList<WorkingHourEntity>()

    fun setData(data: List<WorkingHourEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemCosDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(item, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemCosDetailBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WorkingHourEntity) {
            with(binding) {
                tvDay.text = data.day
                tvTime.text = context.getString(R.string.format_hour, data.startHour, data.endHour)
                ivDelete.setOnClickListener {
                    if (Helper.isConnected(context)) {
                        this@CoSDetailAdapter.data.remove(data)
                        notifyItemRemoved(adapterPosition)
                        cosDetailAction.btnDeletePressed(true, data)
                        Helper.showSnackBar(binding.root, "Item has been deleted!")
                    } else {
                        Helper.showSnackBar(binding.root, "Please check your connection internet!")
                    }
                }
            }
        }

    }

    interface CoSDetailAction {
        fun btnDeletePressed(value: Boolean, data: WorkingHourEntity)
    }

}