package id.stefanusdany.cospace.ui.adminCoS.successfulBooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.databinding.FragmentSuccessfulBookingBinding
import id.stefanusdany.cospace.helper.Helper.visibility

class SuccessfulBookingFragment : Fragment() {

    private var _binding: FragmentSuccessfulBookingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SuccessfulBookingAdapter
    private lateinit var viewModel: SuccessfulBookingViewModel
    private lateinit var bundleData: SuccessfulBookingFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessfulBookingBinding.inflate(inflater, container, false)
        bundleData = SuccessfulBookingFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        getAllSuccessfulBooking()
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
        adapter = SuccessfulBookingAdapter()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(SuccessfulBookingViewModel::class.java)
    }

    private fun getAllSuccessfulBooking() {
        binding.progressBar.visibility(true)
        viewModel.getAllSuccessfulBooking(bundleData.dataLogin.id)
            .observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    with(binding.rvSuccessfulBooking) {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@SuccessfulBookingFragment.adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}