package id.stefanusdany.cospace.ui.adminCoS.adminChat

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.ChatEntity
import id.stefanusdany.cospace.databinding.FragmentAdminDetailChatBinding
import id.stefanusdany.cospace.helper.Helper.visibility
import id.stefanusdany.cospace.ui.user.chat.AdminDetailChatAdapter

class AdminDetailChatFragment : Fragment() {

    private var _binding: FragmentAdminDetailChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var bundleData: AdminDetailChatFragmentArgs
    private lateinit var viewModel: AdminChatViewModel
    private lateinit var adapter: AdminDetailChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDetailChatBinding.inflate(inflater, container, false)
        bundleData = AdminDetailChatFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        setupAdapter()
        getAllDetailChat(bundleData.dataDetailChat.idChat)
        setupAction()
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvWithUser.text = getString(R.string.with_user, bundleData.dataDetailChat.name)
        binding.btnSend.setOnClickListener {
            if (binding.etEnterMessage.text.toString().trim().isNotEmpty()) {
                val data = ChatEntity(
                    System.currentTimeMillis().toString(),
                    bundleData.dataLogin.name,
                    bundleData.dataLogin.photoProfile,
                    binding.etEnterMessage.text.toString().trim(),
                    getDateTime()
                )
                viewModel.sendChatToDatabase(data, bundleData.dataDetailChat.idChat)
                binding.etEnterMessage.text = null


            }
        }
    }

    private fun getDateTime(): String {
        //get current date and time
        val cal = Calendar.getInstance()
        val dt = cal.time
        val sdf = SimpleDateFormat("d MMM yyyy, HH:mm:ss", Locale.US)
        return sdf.format(dt)
    }

    private fun getID(): String {
        val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(20) { alphabet.random() }.joinToString("")
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(AdminChatViewModel::class.java)
    }

    private fun setupAdapter() {
        adapter = AdminDetailChatAdapter { selectedData ->
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllDetailChat(idChat: String) {
        viewModel.getAllDetailChat(idChat).observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                with(binding.rvDetailChat) {
                    layoutManager = LinearLayoutManager(requireContext())
                    smoothScrollToPosition(it.count() - 1)
                    adapter = this@AdminDetailChatFragment.adapter
                    setHasFixedSize(true)
                    binding.rvDetailChat.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
                        if (i4 < i8) {
                            this.postDelayed({
                                binding.rvDetailChat.smoothScrollToPosition(it.count() - 1)
                            }, 100)
                        }
                    }
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


}