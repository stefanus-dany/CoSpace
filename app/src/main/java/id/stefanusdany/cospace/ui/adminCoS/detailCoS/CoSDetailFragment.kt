package id.stefanusdany.cospace.ui.adminCoS.detailCoS

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
import id.stefanusdany.cospace.data.entity.WorkingHourEntity
import id.stefanusdany.cospace.databinding.FragmentCoSDetailBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.visibility

class CoSDetailFragment : Fragment(), CoSDetailAdapter.CoSDetailAction,
    CosDetailDialog.CallbackWorkingHour {
    private var _binding: FragmentCoSDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CoSDetailAdapter
    private lateinit var viewModel: CoSDetailViewModel
    private lateinit var bundleData: CoSDetailFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoSDetailBinding.inflate(inflater, container, false)
        bundleData = CoSDetailFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        getAllWorkingHour()
        getCoWorkingSpaceDetail()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            tvAppBar.text = getString(R.string.cos_facility, bundleData.dataLogin.name)
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAddWorkingHour.setOnClickListener {
                val instance = CosDetailDialog(this@CoSDetailFragment)
                instance.show(childFragmentManager, "SHOW_WORKING_HOUR")
            }
            btnSave.setOnClickListener {
                binding.progressBar.visibility(true)
                when {
                    etPrice.text.toString().isEmpty() || etCapacity.text.toString()
                        .isEmpty() || etAddress.text.toString().isEmpty() -> {
                        Helper.showSnackBar(binding.root, "Please filling all the field!")
                        binding.progressBar.visibility(false)
                    }
                    else -> {
                        if (Helper.isConnected(requireContext())) {
                            val tmp: List<Any> = listOf(
                                etPrice.text.toString().trim().toInt(),
                                etCapacity.text.toString().trim().toInt(),
                                etAddress.text.toString().trim()
                            )
                            viewModel.saveCoWorkingSpaceDetail(bundleData.dataLogin.id, tmp)
                                .observe(viewLifecycleOwner) {
                                    if (it != null) {
                                        Helper.showSnackBar(binding.root, "Changes has been saved!")
                                        findNavController().popBackStack()
                                        binding.progressBar.visibility(false)
                                    } else {
                                        Helper.showSnackBar(binding.root, "Something went wrong!")
                                    }

                                }
                        } else {
                            binding.progressBar.visibility(false)
                            Helper.showSnackBar(
                                binding.root,
                                "Please check your connection internet!"
                            )
                        }
                    }
                }

            }
        }
    }

    private fun setupAdapter() {
        adapter = CoSDetailAdapter(this)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(CoSDetailViewModel::class.java)
    }

    private fun getAllWorkingHour() {
        binding.progressBar.visibility(true)
        viewModel.getAllWorkingHour(bundleData.dataLogin.id)
            .observeOnce(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    with(binding.rvCoSDetail) {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@CoSDetailFragment.adapter
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

    private fun getCoWorkingSpaceDetail() {
        viewModel.getCoWorkingSpaceDetail(bundleData.dataLogin.id).observe(viewLifecycleOwner) {
            binding.apply {
                if (it.isNotEmpty()) {
                    etPrice.setText(it[0].toString())
                    etCapacity.setText(it[1].toString())
                    etAddress.setText(it[2].toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun btnDeletePressed(value: Boolean, data: WorkingHourEntity) {
        if (value) {
            viewModel.deleteWorkingHour(bundleData.dataLogin.id, data)
        }
    }

    override fun callbackWorkingHour(data: WorkingHourEntity) {
        if (Helper.isConnected(requireContext())) {
            viewModel.addWorkingHour(bundleData.dataLogin.id, data).observe(viewLifecycleOwner) {
                Helper.showSnackBar(binding.root, "Item has been added")
                getAllWorkingHour()
            }
        } else {
            Helper.showSnackBar(binding.root, "Please check your connection internet!")
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
}