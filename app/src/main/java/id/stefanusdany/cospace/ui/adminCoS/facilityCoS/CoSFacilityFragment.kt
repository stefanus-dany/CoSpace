package id.stefanusdany.cospace.ui.adminCoS.facilityCoS

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.databinding.FragmentCoSFacilityBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.visibility

class CoSFacilityFragment : Fragment(), FacilityAdapter.FacilityAction,
    FacilityDialog.CallbackFacility {

    private var _binding: FragmentCoSFacilityBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FacilityAdapter
    private lateinit var viewModel: FacilityViewModel
    private lateinit var bundleData: CoSFacilityFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoSFacilityBinding.inflate(inflater, container, false)
        bundleData = CoSFacilityFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        setupAction()
        getAllFacility()
    }

    private fun setupAction() {
        binding.apply {
            tvAppBar.text = getString(R.string.cos_facility, bundleData.dataLogin.name)
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            fabAddFacility.setOnClickListener {
                val instance = FacilityDialog(this@CoSFacilityFragment)
                instance.show(childFragmentManager, "SHOW_FACILITY")
            }
        }
    }

    private fun setupAdapter() {
        adapter = FacilityAdapter(this)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(FacilityViewModel::class.java)
    }

    private fun getAllFacility() {
        binding.progressBar.visibility(true)
        viewModel.getAllFacility(bundleData.dataLogin.id)
            .observeOnce(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    with(binding.rvFacility) {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@CoSFacilityFragment.adapter
                        setHasFixedSize(true)
                    }
                    adapter.setData(it)
                    binding.tvEmpty.visibility(false)
                    binding.progressBar.visibility(false)
                } else {
                    binding.tvEmpty.visibility(true)
                    binding.progressBar.visibility(false)
                }
            }

    }

    //observe once / observe sekali / observe satu kali
    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun btnDeletePressed(value: Boolean, data: FacilityEntity) {
        if (value) {
            viewModel.deleteFacility(bundleData.dataLogin.id, data.id)
        }
    }

    override fun callbackFacility(facility: FacilityEntity) {
        if (Helper.isConnected(requireContext())) {
            viewModel.addFacility(bundleData.dataLogin.id, facility).observe(viewLifecycleOwner) {
                Helper.showSnackBar(binding.root, "${facility.name} has been added")
                getAllFacility()
            }
        } else {
            Helper.showSnackBar(binding.root, "Please check your connection internet!")
        }
    }
}