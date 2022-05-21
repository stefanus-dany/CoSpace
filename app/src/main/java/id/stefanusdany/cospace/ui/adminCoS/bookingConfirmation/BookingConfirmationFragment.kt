package id.stefanusdany.cospace.ui.adminCoS.bookingConfirmation

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
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.databinding.FragmentBookingConfirmationBinding
import id.stefanusdany.cospace.helper.Helper.visibility

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

    override fun btnAcceptPressed(value: Boolean, data: BookingEntity) {
        if (value) {
            viewModel.sendAcceptedBooking(
                bundleData.dataLogin.id,
                data
            ).observe(viewLifecycleOwner){
                if (it){
//                    adapter.notifyItemRemoved(position)
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
        TODO("Not yet implemented")
    }
}