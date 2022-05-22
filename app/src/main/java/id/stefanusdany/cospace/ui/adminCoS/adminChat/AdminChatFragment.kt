package id.stefanusdany.cospace.ui.adminCoS.adminChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.databinding.FragmentAdminChatBinding
import id.stefanusdany.cospace.helper.Helper.visibility


class AdminChatFragment : Fragment() {

    private var _binding: FragmentAdminChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AdminChatViewModel
    private lateinit var adapter: AdminChatAdapter
    private lateinit var bundleData: AdminChatFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminChatBinding.inflate(inflater, container, false)
        bundleData = AdminChatFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        getAllChatsUser()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(AdminChatViewModel::class.java)
    }

    private fun setupAdapter() {
        adapter = AdminChatAdapter { selectedData ->
            val toDetailChatFragment =
                AdminChatFragmentDirections.actionNavigationChatToDetailChatFragment(
                    selectedData,
                    bundleData.dataLogin
                )
            findNavController().navigate(toDetailChatFragment)
        }
    }

    private fun getAllChatsUser() {
        viewModel.getAllChatsCoSpace(bundleData.dataLogin.id).observe(viewLifecycleOwner) {
            if (it != null) {
                val distinct = it.distinct()
                with(binding.rvChat) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@AdminChatFragment.adapter
                    setHasFixedSize(true)
                }
                adapter.setData(distinct)
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