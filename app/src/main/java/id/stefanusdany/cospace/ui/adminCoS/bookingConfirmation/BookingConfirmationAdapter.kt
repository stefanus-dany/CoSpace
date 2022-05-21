package id.stefanusdany.cospace.ui.adminCoS.bookingConfirmation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.databinding.ItemBookingConfirmationBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.showSnackBar

class BookingConfirmationAdapter(private val onButtonPressed: BookingConfirmationAction) :
    RecyclerView.Adapter<BookingConfirmationAdapter.ViewHolder>() {
    private var data = ArrayList<BookingEntity>()
    private lateinit var fragmentManager: FragmentManager

    fun setData(data: List<BookingEntity>?) {
        if (data == null) return
        this.data.clear()
        this.data.addAll(data)
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemBookingConfirmationBinding.inflate(
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
        private val binding: ItemBookingConfirmationBinding,
        private val context: Context
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

                btnPaymentSlip.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable(EXTRA_PAYMENT, data)
                    val timeDialog = PaymentSlipDialog()
                    timeDialog.arguments = bundle
                    timeDialog.show(fragmentManager, "SHOW_DIALOG_PAYMENT_SLIP")
                }

                btnAccept.setOnClickListener {
                    if (Helper.isConnected(context)) {
                        this@BookingConfirmationAdapter.data.remove(data)
                        notifyItemRemoved(adapterPosition)
                        onButtonPressed.btnAcceptPressed(true, data)
                        showSnackBar(binding.root, "Booking has been accepted!")
                    } else {
                        showSnackBar(binding.root, "Please check your connection internet!")
                    }
                }

                btnReject.setOnClickListener {
                    if (Helper.isConnected(context)) {
//                        onButtonPressed.btnRejectPressed(true, data)
                    } else {
                        showSnackBar(binding.root, "Please check your connection internet!")
                    }
                }
            }
        }

    }

    companion object {
        const val EXTRA_PAYMENT = "extra_payment"
    }

    interface BookingConfirmationAction {
        fun btnAcceptPressed(value: Boolean, data: BookingEntity)
        fun btnRejectPressed(value: Boolean, data: BookingEntity)
    }
}