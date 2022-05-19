package id.stefanusdany.cospace.ui.user.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.databinding.FragmentDetailBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.visibility


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private lateinit var bundleData: DetailFragmentArgs
    private lateinit var adapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        bundleData = DetailFragmentArgs.fromBundle(arguments as Bundle)
        setupView()
        setupViewModel()
        setupAdapter()
        setupImageSlider()
        setupPost()
        setupWorkingHour()
        setupPrice()
        setupCapacity()
        setupFacility()
        setupAddress()
        setupAction()
        return binding.root
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        binding.cosDetail.text = getString(R.string.cos_detail, bundleData.dataCoWorkingSpace.name)
        binding.btnChat.text = getString(R.string.chat_cos, bundleData.dataCoWorkingSpace.name)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(DetailViewModel::class.java)
    }

    private fun setupAdapter() {
        adapter = DetailAdapter { selectedData ->

        }
    }

    private fun setupImageSlider() {
        val imagesData = bundleData.dataCoWorkingSpace.images
        val imageList = ArrayList<SlideModel>()
        for (i in imagesData.indices) {
            imageList.add(SlideModel(imagesData[i].url, ScaleTypes.CENTER_INSIDE))
        }
        binding.imageSlider.setImageList(imageList)
    }

    private fun setupPost() {
        val postData = bundleData.dataCoWorkingSpace.post
        if (postData.isNotEmpty()) {
            with(binding.rvPost) {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@DetailFragment.adapter
                setHasFixedSize(true)
            }
            adapter.setData(postData)
            binding.tvEmpty.visibility(false)
            binding.progressBar.visibility(false)
        } else {
            binding.tvEmpty.visibility(true)
            binding.progressBar.visibility(false)
        }
    }

    private fun setupWorkingHour() {
        val workingHourData = bundleData.dataCoWorkingSpace.workingHour
        var dayData = ""
        var timeData = ""
//        val sdf = SimpleDateFormat("hh:mm")
//        val time = sdf.format(workingHourData)
//        val convertStartHour = sdf.format(workingHourData[i].startHour)
//        val convertEndHour = sdf.format(workingHourData[i].startHour)
        for (i in workingHourData.indices) {
            if (i == workingHourData.size - 1) {
                dayData += workingHourData[i].day
                timeData += workingHourData[i].startHour + " - " + workingHourData[i].endHour
            } else {
                dayData += workingHourData[i].day + "\n"
                timeData += workingHourData[i].startHour + " - " + workingHourData[i].endHour + "\n"
            }
        }
        binding.apply {
            tvDay.text = dayData
            tvTime.text = timeData
        }
    }

    private fun setupPrice() {
        val price = bundleData.dataCoWorkingSpace.price
        binding.tvPrice.text = getString(R.string.price_format, price.toString())
    }

    private fun setupCapacity() {
        val capacity = bundleData.dataCoWorkingSpace.capacity
        binding.tvCapacity.text = getString(R.string.format_capacity, capacity.toString())
    }

    private fun setupFacility() {
        val facilityData = bundleData.dataCoWorkingSpace.facility
        var facility = ""

        for (i in facilityData.indices) {
            facility += if (i == facilityData.size - 1) {
                facilityData[i].name
            } else {
                facilityData[i].name + ", "
            }
        }
        binding.tvFacility.text = facility
    }

    private fun setupAddress() {
        val address = bundleData.dataCoWorkingSpace.address
        binding.tvAddress.text = address
    }

    private fun setupAction() {
        binding.apply {
            tvRating.text = bundleData.dataCoWorkingSpace.rating.toString()
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnBooking.setOnClickListener {
                val toBookingFragment =
                    DetailFragmentDirections.actionDetailFragmentToBookingFragment(bundleData.dataCoWorkingSpace)
                findNavController().navigate(toBookingFragment)
            }

            btnMaps.setOnClickListener {
                val uri: Uri =
                    Uri.parse(bundleData.dataCoWorkingSpace.googleMaps)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            btnChat.setOnClickListener {
//                //kondisi jika udah pernah chatan?
//                check udah pernah chatan?
//                val idChat = getIDChat()
//                val idChatEntityUser = IdChatEntity(
//                    bundleData.dataCoWorkingSpace.id,
//                    idChat,
//                    bundleData.dataCoWorkingSpace.name,
//                    bundleData.dataCoWorkingSpace.image
//                )
//                val idChatEntityCoSpace = IdChatEntity(
//                    getUUID(),
//                    idChat,
//                    "Stefanus Dany",
//                    "https://media-exp1.licdn.com/dms/image/C5603AQH9I3jKhWp5BA/profile-displayphoto-shrink_200_200/0/1648087613361?e=1655337600&v=beta&t=-NpbKhYsJIWmxknh6r21RlEscoaYGOsTAMyQrkPa1fM"
//                )
//                viewModel.creatingChats(idChatEntityUser, idChatEntityCoSpace)
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

    private fun getIDChat(): String {
        val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(20) { alphabet.random() }.joinToString("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}