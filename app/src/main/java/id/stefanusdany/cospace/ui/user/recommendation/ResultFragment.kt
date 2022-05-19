package id.stefanusdany.cospace.ui.user.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.data.entity.ResultRecommendationEntity
import id.stefanusdany.cospace.databinding.FragmentResultBinding
import id.stefanusdany.cospace.helper.Helper.visibility
import id.stefanusdany.cospace.ui.user.home.HomeAdapter
import id.stefanusdany.cospace.ui.user.home.HomeFragmentDirections

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var bundleData: ArrayList<ResultRecommendationEntity>
    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        bundleData =
            arguments?.getSerializable(EXTRA_HASHMAP) as ArrayList<ResultRecommendationEntity>
        setupView()
        setupViewModel()
        setupAdapter()
        getAllCoWorkingSpace()
        setupAction()
        return binding.root
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(ResultViewModel::class.java)
    }

    private fun setupAdapter() {
        adapter = HomeAdapter { selectedData ->
            val toDetailFragment =
                ResultFragmentDirections.actionResultFragmentToDetailFragment(selectedData)
            findNavController().navigate(toDetailFragment)
        }
    }

    private fun sortRecommendation(data: List<CoWorkingSpaceEntity>): List<CoWorkingSpaceEntity> {
        val tmp = mutableListOf<CoWorkingSpaceEntity>()
        for (i in data.indices) {
            for (j in data.indices) {
                if (bundleData[i].id == data[j].id) {
                    tmp.add(data[j])
                }
            }
        }
        return tmp
    }

    private fun getAllCoWorkingSpace() {
        binding.progressBar.visibility(true)
        viewModel.getAllCoWorkingSpace()
            .observe(viewLifecycleOwner) { listCoWorkingSpace ->
                if (listCoWorkingSpace != null) {
                    with(binding.rvHome) {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@ResultFragment.adapter
                        setHasFixedSize(true)
                    }
                    adapter.setData(sortRecommendation(listCoWorkingSpace))
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

    companion object {
        const val EXTRA_HASHMAP = "extra_hashmap"
    }

}