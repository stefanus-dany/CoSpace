package id.stefanusdany.cospace.ui.adminCoS.bookingConfirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.data.entity.ChatEntity
import id.stefanusdany.cospace.databinding.DialogPaymentSlipBinding
import id.stefanusdany.cospace.helper.Helper.loadImage

class PaymentSlipDialog : DialogFragment() {

    private var _binding: DialogPaymentSlipBinding? = null
    private val binding get() = _binding!!
    private lateinit var bundleData: BookingEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPaymentSlipBinding.inflate(inflater, container, false)
        bundleData = arguments?.getParcelable(EXTRA_PAYMENT) ?: BookingEntity()
        setupAction()
        return binding.root
    }

    private fun setupAction() {
        binding.apply {
            ivPaymentSlip.loadImage(bundleData.paymentSlip)
            btnDone.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_PAYMENT = "extra_payment"
    }
}