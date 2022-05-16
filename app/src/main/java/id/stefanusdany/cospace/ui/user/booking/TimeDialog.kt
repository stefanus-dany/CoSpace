package id.stefanusdany.cospace.ui.user.booking

import android.os.Bundle
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.databinding.DialogTimeBinding
import id.stefanusdany.cospace.helper.Helper.showSnackBar

class TimeDialog(private val callback: CallbackTime) : DialogFragment() {

    private var _binding: DialogTimeBinding? = null
    private val binding get() = _binding!!
    private var startHour = ""
    private var startMinute = ""
    private var endHour = ""
    private var endMinute = ""
    private lateinit var bundleData: CoWorkingSpaceEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTimeBinding.inflate(inflater, container, false)
        bundleData = arguments?.getParcelable(EXTRA_DATA) ?: CoWorkingSpaceEntity()
        setSpinnerTimeAdapter()
        setupSpinnerAction()
        setupAction()
        setupBookedByOther()
        return binding.root
    }

    private fun setupAction() {
        binding.apply {
            btnConfirm.setOnClickListener {
                if (startHour.isNotEmpty() && startMinute.isNotEmpty() && endHour.isNotEmpty() && endMinute.isNotEmpty()){
                    callback.callbackTime(startHour, startMinute, endHour, endMinute)
                    dialog?.dismiss()
                } else {
                    showSnackBar(binding.root, "Please filling all the field!")
                }

            }
        }

    }

    private fun setSpinnerTimeAdapter() {
        binding.spinnerStartHour.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_hour)
            )
        )

        binding.spinnerStartMinute.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_minute)
            )
        )

        binding.spinnerEndHour.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_hour)
            )
        )

        binding.spinnerEndMinute.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_minute)
            )
        )
    }

    private fun setupBookedByOther(){
        var bookingData = ""
        for (i in 0 until bundleData.booking.size) {
            bookingData += if (i == bundleData.booking.size - 1) {
                bundleData.booking[i].startHour + " - " + bundleData.booking[i].endHour
            } else {
                bundleData.booking[i].startHour + " - " + bundleData.booking[i].endHour + "\n"
            }
        }
        binding.tvBookedTime.text = bookingData
    }

    private fun setupSpinnerAction() {
        binding.apply {
            spinnerStartHour.setOnItemClickListener { parent, view, position, rowId ->
                startHour = parent?.getItemAtPosition(position).toString()
//                binding.etFilterCity.text = null
            }

            spinnerStartMinute.setOnItemClickListener { parent, view, position, rowId ->
                startMinute = parent?.getItemAtPosition(position).toString()
//                binding.etFilterCity.text = null
            }

            spinnerEndHour.setOnItemClickListener { parent, view, position, rowId ->
                endHour = parent?.getItemAtPosition(position).toString()
//                binding.etFilterCity.text = null
            }

            spinnerEndMinute.setOnItemClickListener { parent, view, position, rowId ->
                endMinute = parent?.getItemAtPosition(position).toString()
//                binding.etFilterCity.text = null
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface CallbackTime{
        fun callbackTime(startHour: String, startMinute: String, endHour: String, endMinute: String)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}