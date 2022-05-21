package id.stefanusdany.cospace.ui.adminCoS.detailCoS

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.WorkingHourEntity
import id.stefanusdany.cospace.databinding.DialogCosDetailBinding
import id.stefanusdany.cospace.helper.Helper.showSnackBar

class CosDetailDialog(private val callback: CallbackWorkingHour) : DialogFragment() {

    private var _binding: DialogCosDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var workingHourList: List<WorkingHourEntity>
    private var workingHourData: WorkingHourEntity = WorkingHourEntity()
    private var startHour = ""
    private var startMinute = ""
    private var endHour = ""
    private var endMinute = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCosDetailBinding.inflate(inflater, container, false)
        workingHourList = initWorkingHourList()
        setSpinnerTimeAdapter()
        setupSpinnerAction()
        setupAction()
        return binding.root
    }

    private fun initWorkingHourList(): List<WorkingHourEntity> {
        val array = resources.getStringArray(R.array.adapter_working_hour)
        val delimit = ","
        val data = arrayListOf<WorkingHourEntity>()
        for (i in array.indices) {
            val tmpData = array[i].split(delimit).toTypedArray()
            val id = tmpData[0]
            val name = tmpData[1]
            data.add(WorkingHourEntity(id = id, day = name))
        }
        return data
    }

    private fun setSpinnerTimeAdapter() {
        val list = arrayListOf<String>()
        for (i in workingHourList.indices) {
            list.add(workingHourList[i].day)
        }
        binding.spinnerDay.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                list
            )
        )

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

    private fun setupSpinnerAction() {
        binding.apply {
            spinnerDay.setOnItemClickListener { parent, view, position, rowId ->
                val tmp = parent?.getItemAtPosition(position).toString()
                for (i in workingHourList.indices) {
                    if (tmp == workingHourList[i].day) {
                        workingHourData = workingHourList[i]
                    }
                }
            }

            spinnerStartHour.setOnItemClickListener { parent, view, position, rowId ->
                startHour = parent?.getItemAtPosition(position).toString()
            }

            spinnerStartMinute.setOnItemClickListener { parent, view, position, rowId ->
                startMinute = parent?.getItemAtPosition(position).toString()
            }

            spinnerEndHour.setOnItemClickListener { parent, view, position, rowId ->
                endHour = parent?.getItemAtPosition(position).toString()
            }

            spinnerEndMinute.setOnItemClickListener { parent, view, position, rowId ->
                endMinute = parent?.getItemAtPosition(position).toString()
            }
        }

    }


    private fun setupAction() {
        binding.apply {
            btnConfirm.setOnClickListener {
                if (workingHourData.day.isNotEmpty() && startHour.isNotEmpty() && startMinute.isNotEmpty() && endHour.isNotEmpty() && endMinute.isNotEmpty()) {
                    workingHourData.startHour =
                        getString(R.string.format_time, startHour, startMinute)
                    workingHourData.endHour = getString(R.string.format_time, endHour, endMinute)
                    callback.callbackWorkingHour(workingHourData)
                    dialog?.dismiss()
                } else {
                    showSnackBar(binding.root, "Please filling all the field!")
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface CallbackWorkingHour {
        fun callbackWorkingHour(data: WorkingHourEntity)
    }
}