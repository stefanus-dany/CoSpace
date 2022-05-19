package id.stefanusdany.cospace.ui.chat

import android.content.Context
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
import id.stefanusdany.cospace.databinding.FragmentChatBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.visibility


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        setupAdapter()
        getAllChatsUser()
    }

    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(ChatViewModel::class.java)
    }

    private fun setupAdapter() {
        adapter = ChatAdapter { selectedData ->
            val toDetailChatFragment = ChatFragmentDirections.actionNavigationChatToDetailChatFragment(selectedData)
            findNavController().navigate(toDetailChatFragment)
        }
    }

    private fun getAllChatsUser() {
        viewModel.getAllChatsUser(getUUID()).observe(viewLifecycleOwner) {
            if (it != null) {
                with(binding.rvChat) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@ChatFragment.adapter
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

    private fun getUUID(): String {
        val sp = activity?.getSharedPreferences(Helper.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        if (sp != null) {
            return sp.getString(Helper.UUID, "").toString()
        }
        return ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}