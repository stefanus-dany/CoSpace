package id.stefanusdany.cospace.ui.adminCoS.successfulBooking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.databinding.ItemSuccessfulBookingBinding

class SuccessfulBookingAdapter :
    RecyclerView.Adapter<SuccessfulBookingAdapter.ViewHolder>() {
    private var data = ArrayList<BookingEntity>()

    fun setData(data: List<BookingEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemSuccessfulBookingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemSuccessfulBookingBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: BookingEntity) {
            with(binding) {
                tvDate.text = data.date
                tvHour.text =
                    itemView.resources.getString(R.string.format_hour, data.startHour, data.endHour)
                tvName.text = data.name
                tvEmail.text = data.email
                tvPhone.text = data.phoneNumber
                tvCapacity.text =
                    itemView.resources.getString(R.string.format_capacity, data.capacity.toString())
                tvPrice.text = itemView.resources.getString(
                    R.string.price_format_default,
                    data.totalPrice.toString()
                )
            }
        }

    }
}