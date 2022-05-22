package id.stefanusdany.cospace.ui.user.chat

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import id.stefanusdany.cospace.databinding.FragmentDetailChatBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.visibility

class DetailChatFragment : Fragment() {

    private var _binding: FragmentDetailChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var bundleData: DetailChatFragmentArgs
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: DetailChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailChatBinding.inflate(inflater, container, false)
        bundleData = DetailChatFragmentArgs.fromBundle(arguments as Bundle)
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
                    getUserName(),
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/User_font_awesome.svg/2048px-User_font_awesome.svg.png",
                    binding.etEnterMessage.text.toString().trim(),
                    getDateTime()
                )
                viewModel.sendChatToDatabase(data, bundleData.dataDetailChat.idChat)
                binding.etEnterMessage.text = null
            }

        }
    }

    private fun getUserName(): String {
        val sp = activity?.getSharedPreferences(Helper.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        if (sp != null) {
            return sp.getString(Helper.NAME, "").toString()
        }
        return ""
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
        viewModel = factory.create(ChatViewModel::class.java)
    }

    private fun setupAdapter() {
        adapter = DetailChatAdapter { selectedData ->
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllDetailChat(idChat: String) {
        viewModel.getAllDetailChat(idChat).observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                with(binding.rvDetailChat) {
                    layoutManager = LinearLayoutManager(requireContext())
                    smoothScrollToPosition(it.count() - 1)
                    adapter = this@DetailChatFragment.adapter
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