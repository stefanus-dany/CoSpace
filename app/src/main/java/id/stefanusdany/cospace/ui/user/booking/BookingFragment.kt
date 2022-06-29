package id.stefanusdany.cospace.ui.user.booking

import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.databinding.FragmentBookingBinding
import id.stefanusdany.cospace.helper.Helper.showSnackBar

class BookingFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimeDialog.CallbackTime {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    private lateinit var bundleData: BookingFragmentArgs
    private var day = 0
    private var month = 0
    private var year = 0

    private var saveDay = 0
    private var saveMonth = 0
    private var saveYear = 0

    private var startHour = ""
    private var startMinute = ""
    private var endHour = ""
    private var endMinute = ""

    private var totalTime = ""
    private var totalPrice = 0

    private var selectedDate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        bundleData = BookingFragmentArgs.fromBundle(arguments as Bundle)
        setupView()
        setupAction()
        return binding.root
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        binding.tvAppbar.text =
            getString(R.string.booking_cos_format, bundleData.dataCoWorkingSpace.name)
    }

    private fun getDateCalendar() {
        Calendar.getInstance().apply {
            day = this.get(DAY_OF_MONTH)
            month = this.get(Calendar.MONTH)
            year = this.get(Calendar.YEAR)
        }
    }

    private fun setupAction() {
        binding.apply {

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            spinnerDay.setOnClickListener {
                getDateCalendar()
                DatePickerDialog(requireContext(), this@BookingFragment, year, month, day).apply {
                    this.datePicker.minDate = System.currentTimeMillis() - 1000
                    show()
                }
            }

            spinnerTime.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_DATA, bundleData.dataCoWorkingSpace)
                val timeDialog = TimeDialog(this@BookingFragment, selectedDate)
                timeDialog.arguments = bundle
                timeDialog.show(childFragmentManager, "SHOW_DIALOG")
            }

            btnBookingNow.setOnClickListener {
                when {
                    binding.etName.text.toString().trim().isEmpty() -> {
                        binding.etName.error = getString(R.string.insert_name)
                    }

                    binding.etEmail.text.toString().trim().isEmpty() -> {
                        binding.etEmail.error = getString(R.string.insert_email)
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim())
                        .matches() -> {
                        binding.etEmail.error = getString(R.string.email_is_not_valid)
                    }

                    binding.etPhoneNumber.text.toString().trim().isEmpty() -> {
                        binding.etEmail.error = getString(R.string.insert_phone)
                    }

                    binding.spinnerDay.text.toString().trim().isEmpty() -> {
                        binding.spinnerDay.error = getString(R.string.label_day)
                    }

                    binding.spinnerTime.text.toString().trim().isEmpty() -> {
                        binding.spinnerTime.error = getString(R.string.label_time)
                    }

                    binding.etCapacity.text.toString().trim().isEmpty() -> {
                        binding.etCapacity.error = getString(R.string.insert_capacity)
                    }

                    day == 0 && month == 0 && year == 0 && saveDay == 0 && saveMonth == 0 && saveYear == 0
                            && this@BookingFragment.totalPrice == 0 && startHour.isEmpty() && startMinute.isEmpty() && endHour.isEmpty() &&
                            endMinute.isEmpty() && this@BookingFragment.totalTime.isEmpty() -> {
                        showSnackBar(binding.root, "Please filling all the field!")
                    }

                    else -> {
                        val dataBooking = BookingEntity(
                            id = getID(),
                            capacity = binding.etCapacity.text.toString().toInt(),
                            date = getString(
                                R.string.format_date,
                                saveDay.toString(),
                                saveMonth.toString(),
                                saveYear.toString()
                            ),
                            paymentSlip = "",
                            phoneNumber = binding.etPhoneNumber.text.toString().trim(),
                            name = binding.etName.text.toString().trim(),
                            email = binding.etEmail.text.toString().trim(),
                            startHour = getString(R.string.format_time, startHour, startMinute),
                            endHour = getString(R.string.format_time, endHour, endMinute),
                            timeDuration = binding.totalTime.text.toString(),
                            totalPrice = this@BookingFragment.totalPrice
                        )
                        val toPaymentFragment =
                            BookingFragmentDirections.actionBookingFragmentToPaymentFragment(
                                dataBooking,
                                bundleData.dataCoWorkingSpace
                            )
                        findNavController().navigate(toPaymentFragment)
                    }
                }
            }
        }
    }

    private fun getID(): String {
        val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(20) { alphabet.random() }.joinToString("")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        saveDay = p3
        saveMonth = p2
        saveYear = p1
        selectedDate = "$p3-$p2-$p1"
        binding.spinnerDay.setText(selectedDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun callbackTime(
        startHour: String,
        startMinute: String,
        endHour: String,
        endMinute: String
    ) {
        this@BookingFragment.apply {
            this.startHour = startHour
            this.startMinute = startMinute
            this.endHour = endHour
            this.endMinute = endMinute
        }
        binding.spinnerTime.setText("$startHour:$startMinute - $endHour:$endMinute")
        val totalHour = (endHour.toInt() - startHour.toInt()) * 60
        val totalMinute = (endMinute.toInt() - startMinute.toInt())
        val totalTimeInMinute = totalHour + totalMinute

        val hour = totalTimeInMinute / 60
        val minute = totalTimeInMinute % 60

        totalTime = "$hour Hour(s) $minute Minute(s)"

        binding.totalTime.text = totalTime
        binding.price.text =
            getString(R.string.price_format, bundleData.dataCoWorkingSpace.price.toString())
        val priceHour = hour * bundleData.dataCoWorkingSpace.price
        var priceMinute = 0
        if (minute == 30) {
            priceMinute = (bundleData.dataCoWorkingSpace.price.toDouble() / 2).toInt()
        }
        totalPrice = priceHour + priceMinute
        binding.totalPrice.text = getString(R.string.price_format_default, totalPrice.toString())
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}