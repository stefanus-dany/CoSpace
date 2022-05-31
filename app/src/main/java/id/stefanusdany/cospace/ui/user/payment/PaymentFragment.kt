package id.stefanusdany.cospace.ui.user.payment

import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.databinding.FragmentPaymentBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.showSnackBar
import id.stefanusdany.cospace.helper.Helper.visibility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private var uriPaymentSlip: Uri? = null
    private lateinit var viewModel: PaymentViewModel
    private lateinit var bundleData: PaymentFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        bundleData = PaymentFragmentArgs.fromBundle(arguments as Bundle)
        setupView()
        setupViewModel()
        setupAction()
        return binding.root
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun setupAction() {
        binding.btnUploadPaymentSlip.setOnClickListener {
            chooseAFile()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.totalPrice.text = bundleData.dataBooking.totalPrice.toString()

        binding.btnIHavePaid.setOnClickListener {
            when (binding.ivPaymentSlip.visibility) {
                View.GONE ->
                    showSnackBar(binding.root, "Please upload a payment slip!")
                else -> {
                    binding.progressBar.visibility(true)
                    viewModel.uploadBooking(
                        bundleData.dataBooking,
                        bundleData.dataCoWorkingSpace.id,
                        bundleData.dataBooking.id
                    ).observe(viewLifecycleOwner) {
                        if (it == 1) {
                            viewModel.uploadPaymentSlip(
                                uriPaymentSlip,
                                bundleData.dataCoWorkingSpace.id,
                                bundleData.dataBooking.id
                            ).observe(viewLifecycleOwner) { result ->
                                binding.progressBar.visibility(false)
                                if (result == 1) {
                                    sendEmailBooking(requireContext())
                                    showSnackBar(binding.root, "Your booking has been successful")
                                    findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
                                } else {
                                    showSnackBar(binding.root, "Error upload payment slip")
                                }
                            }
                        } else {
                            showSnackBar(binding.root, "Error creating a booking")
                            binding.progressBar.visibility(false)
                        }
                    }
                }
            }
        }
    }

    private fun chooseAFile() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            uriPaymentSlip = selectedImg
            binding.ivPaymentSlip.visibility = View.VISIBLE
            binding.ivPaymentSlip.setImageURI(selectedImg)
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(PaymentViewModel::class.java)
    }

    private fun sendEmailBooking(ctx: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            val props = Properties().apply {
                setProperty("mail.transport.protocol", "smtp")
                setProperty("mail.host", "smtp.gmail.com")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", "465")
                put("mail.debug", "true")
                put("mail.smtp.socketFactory.port", "465")
                put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory")
                put("mail.smtp.socketFactory.fallback", "false")
            }
            val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(Helper.EMAIL_COSPACE, Helper.PASS_COSPACE)
                }
            })

            try {
                val transport = session.transport
                val message = MimeMessage(session).apply {
                    sender = InternetAddress(Helper.EMAIL_COSPACE)
                    addRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(bundleData.dataBooking.email)
                    )
                    subject = "Your booking has been sent to ${bundleData.dataCoWorkingSpace.name}!"
                    setText(ctx.getString(
                        R.string.format_message_booking,
                        bundleData.dataCoWorkingSpace.name,
                        bundleData.dataCoWorkingSpace.name,
                        bundleData.dataBooking.id,
                        bundleData.dataBooking.name,
                        bundleData.dataBooking.email,
                        bundleData.dataBooking.phoneNumber,
                        bundleData.dataBooking.date,
                        bundleData.dataBooking.startHour,
                        bundleData.dataBooking.endHour,
                        bundleData.dataBooking.capacity.toString(),
                        bundleData.dataCoWorkingSpace.name,
                        "0895424014701"
                    ))
                }
                transport.connect()
                Transport.send(message)
                transport.close()
            } catch (e: MessagingException) {
                Log.e(Helper.TAG, "sendEmailBooking: ${e.message}" )
            }
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }
}