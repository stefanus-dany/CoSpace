package id.stefanusdany.cospace.ui.adminCoS.facilityCoS

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.databinding.ItemFacilityBinding
import id.stefanusdany.cospace.helper.Helper

class FacilityAdapter(private val facilityAction: FacilityAction) :
    RecyclerView.Adapter<FacilityAdapter.ViewHolder>() {
    private var data = ArrayList<FacilityEntity>()

    fun setData(data: List<FacilityEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemFacilityBinding.inflate(
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
        private val binding: ItemFacilityBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FacilityEntity) {
            with(binding) {
                tvName.text = data.name

                ivDelete.setOnClickListener {
                    if (Helper.isConnected(context)) {
                        this@FacilityAdapter.data.remove(data)
                        notifyItemRemoved(adapterPosition)
                        facilityAction.btnDeletePressed(true, data)
                        Helper.showSnackBar(binding.root, "${data.name} has been deleted!")
                    } else {
                        Helper.showSnackBar(binding.root, "Please check your connection internet!")
                    }
                }
            }
        }

    }

    interface FacilityAction {
        fun btnDeletePressed(value: Boolean, data: FacilityEntity)
    }

}