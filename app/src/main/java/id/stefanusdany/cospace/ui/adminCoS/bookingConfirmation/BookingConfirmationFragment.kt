package id.stefanusdany.cospace.ui.adminCoS.bookingConfirmation

import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.databinding.FragmentBookingConfirmationBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.visibility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingConfirmationFragment : Fragment(),
    BookingConfirmationAdapter.BookingConfirmationAction {

    private var _binding: FragmentBookingConfirmationBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BookingConfirmationAdapter
    private lateinit var viewModel: BookingConfirmationViewModel
    private lateinit var bundleData: BookingConfirmationFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingConfirmationBinding.inflate(inflater, container, false)
        bundleData = BookingConfirmationFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        getAllBookingConfirmation()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupAdapter() {
        adapter = BookingConfirmationAdapter(this)
        adapter.setFragmentManager(childFragmentManager)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(BookingConfirmationViewModel::class.java)
    }

    private fun getAllBookingConfirmation() {
        binding.progressBar.visibility(true)
        viewModel.getAllBookingConfirmation(bundleData.dataLogin.id)
            .observeOnce(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    with(binding.rvBookingConfirmation) {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@BookingConfirmationFragment.adapter
                        setHasFixedSize(true)
                    }
                    val sortedData = it.sortedByDescending {
                        it.timestamp
                    }
                    adapter.setData(sortedData)
                    binding.tvEmpty.visibility(false)
                    binding.progressBar.visibility(false)
                } else {
                    binding.tvEmpty.visibility(true)
                    binding.progressBar.visibility(false)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun btnAcceptPressed(value: Boolean, data: BookingEntity) {
        if (value) {
            viewModel.sendAcceptedBooking(
                bundleData.dataLogin.id,
                data
            ).observe(viewLifecycleOwner) {
                if (it) {
                    sendEmailBooking(requireContext(), data, isAccepted = true)
                    getAllBookingConfirmation()
                }
            }

        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun btnRejectPressed(value: Boolean, data: BookingEntity) {
        val etReason = EditText(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Insert your reason!")
            .setView(etReason)
            .setCancelable(true)
            .setPositiveButton("Reject", null)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        val btnConfirm = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
        btnConfirm.setOnClickListener {
            if (etReason.text.toString().trim().isNotEmpty()) {
                if (value) {
                    viewModel.deleteAcceptedBooking(
                        bundleData.dataLogin.id,
                        data
                    ).observe(viewLifecycleOwner) {
                        if (it) {
                            getAllBookingConfirmation()
                        }
                    }
                    sendEmailBooking(
                        requireContext(),
                        data,
                        isAccepted = false,
                        reason = etReason.text.toString().trim()
                    )
                }
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please insert your reason!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun sendEmailBooking(
        ctx: Context,
        dataBooking: BookingEntity,
        isAccepted: Boolean,
        reason: String = ""
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val props = Properties().apply {
                setProperty("mail.transport.protocol", "smtp")
                setProperty("mail.host", "smtp.gmail.com")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", "465")
                put("mail.debug", "true")
                put("mail.smtp.socketFactory.port", "465")
                put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
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
                        InternetAddress.parse(dataBooking.email)
                    )
                    if (isAccepted) {
                        subject = "Your booking has been accepted by ${bundleData.dataLogin.name}!"
                        setText(
                            ctx.getString(
                                R.string.format_message_success_booking,
                                dataBooking.name,
                                bundleData.dataLogin.name,
                                dataBooking.id,
                                dataBooking.name,
                                dataBooking.email,
                                dataBooking.phoneNumber,
                                dataBooking.date,
                                dataBooking.startHour,
                                dataBooking.endHour,
                                dataBooking.capacity.toString()
                            )
                        )
                    } else {
                        subject = "Your booking has been rejected by ${bundleData.dataLogin.name}!"
                        setText(
                            ctx.getString(
                                R.string.format_message_rejected_booking,
                                dataBooking.name,
                                bundleData.dataLogin.name,
                                dataBooking.id,
                                dataBooking.name,
                                dataBooking.email,
                                dataBooking.phoneNumber,
                                dataBooking.date,
                                dataBooking.startHour,
                                dataBooking.endHour,
                                dataBooking.capacity.toString(),
                                reason
                            )
                        )
                    }

                }
                transport.connect()
                Transport.send(message)
                transport.close()
            } catch (e: MessagingException) {
                Log.e(Helper.TAG, "sendEmailBooking: ${e.message}")
            }
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

    }
}