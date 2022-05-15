package id.stefanusdany.cospace.ui.user.booking

import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.databinding.FragmentBookingBinding

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
                val timeDialog = TimeDialog(this@BookingFragment)
                timeDialog.arguments = bundle
                timeDialog.show(childFragmentManager, "SHOW_DIALOG")
            }
        }
    }


    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        saveDay = p3
        saveMonth = p2
        saveYear = p1
        Snackbar.make(
            binding.root,
            "Day: $saveDay, Month: $saveMonth, Year: $saveYear",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
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

        val totalTime = "$hour Hour $minute Minutes"

        binding.totalTime.text = totalTime
        binding.price.text =
            getString(R.string.price_format, bundleData.dataCoWorkingSpace.price.toString())
        val priceHour = hour * bundleData.dataCoWorkingSpace.price
        var priceMinute = 0
        if (minute == 30) {
            priceMinute = (bundleData.dataCoWorkingSpace.price.toDouble() / 2).toInt()
        }
        val totalPrice = priceHour + priceMinute
        binding.totalPrice.text = totalPrice.toString()
    }
}