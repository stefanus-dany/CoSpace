package id.stefanusdany.cospace.ui.user.detail

import kotlin.math.roundToInt
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.data.entity.IdChatEntity
import id.stefanusdany.cospace.data.entity.TopsisEntity
import id.stefanusdany.cospace.databinding.FragmentDetailBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.ui.user.recommendation.RecommendationFragment.Companion.EXTRA_TOPSIS_DATA
import id.stefanusdany.cospace.ui.user.recommendation.ResultFragment.Companion.EXTRA_SELECTED_DATA


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private lateinit var bundleData: CoWorkingSpaceEntity
    private var bundleTopsis = listOf<TopsisEntity>()
    private lateinit var adapter: DetailAdapter
    private var idChat: String = getIDChat()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        bundleData =
            arguments?.getParcelable<CoWorkingSpaceEntity>(EXTRA_SELECTED_DATA) as CoWorkingSpaceEntity
        bundleTopsis =
            arguments?.getParcelableArrayList(EXTRA_TOPSIS_DATA) ?: listOf()
        setupView()
        setupViewModel()
        setupAdapter()
        setupImageSlider()
        setupWorkingHour()
        setupDistance()
        setupPrice()
        setupCapacity()
        setupFacility()
        setupAddress()
        setupAction()
        return binding.root
    }

    private fun setupDistance() {
        binding.tvDistance.apply {
            isVisible = !bundleTopsis.isNullOrEmpty()
            if (bundleTopsis.isNullOrEmpty()) return
            for (i in bundleTopsis) {
                if (i.id == bundleData.id) {
                    val roundNumber = (i.distance * 100.0).roundToInt() / 100.0
                    text = roundNumber.toString() + " Km"
//                    text = i.distance.toString()
                }
            }
        }
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        binding.cosDetail.text = getString(R.string.cos_detail, bundleData.name)
        binding.btnChat.text = getString(R.string.chat_cos, bundleData.name)
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
        val imagesData = bundleData.images
        val imageList = ArrayList<SlideModel>()
        for (i in imagesData.indices) {
            imageList.add(SlideModel(imagesData[i].url, ScaleTypes.CENTER_INSIDE))
        }
        binding.imageSlider.setImageList(imageList)
    }

    private fun setupWorkingHour() {
        val workingHourData = bundleData.workingHour
        var dayData = ""
        var timeData = ""
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
        val price = bundleData.price
        binding.tvPrice.text = getString(R.string.price_format, price.toString())
    }

    private fun setupCapacity() {
        val capacity = bundleData.capacity
        binding.tvCapacity.text = getString(R.string.format_capacity, capacity.toString())
    }

    private fun setupFacility() {
        val facilityData = bundleData.facility
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
        val address = bundleData.address
        binding.tvAddress.text = address
    }

    private fun setupAction() {
        binding.apply {
            tvRating.text = bundleData.rating.toString()
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnBooking.setOnClickListener {
                val toBookingFragment =
                    DetailFragmentDirections.actionDetailFragmentToBookingFragment(bundleData)
                findNavController().navigate(toBookingFragment)
            }

            btnMaps.setOnClickListener {
                val uri: Uri =
                    Uri.parse(bundleData.googleMaps)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            btnChat.setOnClickListener {
                viewModel.isExistChatWithCoSpace(getUUID(), bundleData.id)
                    .observe(viewLifecycleOwner) { idChatEntity ->
                        if (idChatEntity.idChat.isNotEmpty()) {
                            val toDetailChat =
                                DetailFragmentDirections.actionDetailFragmentToDetailChatFragment(
                                    idChatEntity
                                )
                            findNavController().navigate(toDetailChat)
                        } else {
                            val idChat = this@DetailFragment.idChat
                            val idChatEntityUser = IdChatEntity(
                                bundleData.id,
                                idChat,
                                bundleData.name,
                                bundleData.image
                            )
                            val idChatEntityCoSpace = IdChatEntity(
                                getUUID(),
                                idChat,
                                getUsername(),
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/User_font_awesome.svg/2048px-User_font_awesome.svg.png"
                            )
                            viewModel.creatingChats(idChatEntityUser, idChatEntityCoSpace)
                                .observe(viewLifecycleOwner) { success ->
                                    if (success) {
                                        val toDetailChat =
                                            DetailFragmentDirections.actionDetailFragmentToDetailChatFragment(
                                                idChatEntity
                                            )
                                        findNavController().navigate(toDetailChat)
                                    }
                                }
                        }
                    }

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

    private fun getUsername(): String {
        val sp = activity?.getSharedPreferences(Helper.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        if (sp != null) {
            return sp.getString(Helper.NAME, "").toString()
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