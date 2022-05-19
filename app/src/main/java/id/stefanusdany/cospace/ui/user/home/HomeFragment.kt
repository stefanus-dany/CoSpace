package id.stefanusdany.cospace.ui.user.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.databinding.FragmentHomeBinding
import id.stefanusdany.cospace.helper.Helper.visibility

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        setupAdapter()
        getAllCoWorkingSpace()
    }

    private fun setupAdapter() {
        adapter = HomeAdapter { selectedData ->
            val toDetailFragment =
                HomeFragmentDirections.actionNavigationHomeToDetailFragment(selectedData)
            findNavController().navigate(toDetailFragment)
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(HomeViewModel::class.java)
    }

    private fun getAllCoWorkingSpace() {
        binding.progressBar.visibility(true)
        viewModel.getAllCoWorkingSpace()
            .observe(viewLifecycleOwner) { listCoWorkingSpace ->
                if (listCoWorkingSpace != null) {
                    with(binding.rvHome) {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@HomeFragment.adapter
                        setHasFixedSize(true)
                    }
                    adapter.setData(listCoWorkingSpace)
                    binding.tvEmpty.visibility(false)
                    binding.progressBar.visibility(false)
                } else {
                    binding.tvEmpty.visibility(true)
                    binding.progressBar.visibility(false)
                }
            }

    }

    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}