package id.stefanusdany.cospace.ui.adminCoS.facilityCoS

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.databinding.DialogFacilityBinding
import id.stefanusdany.cospace.helper.Helper.showSnackBar

class FacilityDialog(private val callback: CallbackFacility) : DialogFragment() {

    private var _binding: DialogFacilityBinding? = null
    private val binding get() = _binding!!
    private lateinit var facilityList: List<FacilityEntity>
    private var facilityData: FacilityEntity = FacilityEntity()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFacilityBinding.inflate(inflater, container, false)
        facilityList = initFacilityList()
        setSpinnerAdapter()
        setupSpinnerAction()
        setupAction()
        return binding.root
    }

    private fun initFacilityList(): List<FacilityEntity> {
        val array = resources.getStringArray(R.array.adapter_facility)
        val delimit = ","
        val data = arrayListOf<FacilityEntity>()
        for (i in array.indices) {
            val tmpData = array[i].split(delimit).toTypedArray()
            val id = tmpData[0]
            val name = tmpData[1]
            data.add(FacilityEntity(id, name))
        }
        return data
    }

    private fun setSpinnerAdapter() {
        val list = arrayListOf<String>()
        for (i in facilityList.indices) {
            list.add(facilityList[i].name)
        }
        binding.spinnerFacility.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                list
            )
        )
    }

    private fun setupSpinnerAction() {
        binding.spinnerFacility.setOnItemClickListener { parent, view, position, rowId ->
            val tmp = parent?.getItemAtPosition(position).toString()
            for (i in facilityList.indices) {
                if (tmp == facilityList[i].name) {
                    facilityData = facilityList[i]
                }
            }
        }

    }

    private fun setupAction() {
        binding.apply {
            btnConfirm.setOnClickListener {
                if (facilityData.id.isNotEmpty() && facilityData.name.isNotEmpty()) {
                    callback.callbackFacility(facilityData)
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

    interface CallbackFacility {
        fun callbackFacility(facility: FacilityEntity)
    }

}