package id.stefanusdany.cospace.ui.user.recommendation

import java.lang.Math.toRadians
import kotlin.math.pow
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.data.entity.TopsisEntity
import id.stefanusdany.cospace.databinding.FragmentRecommendationBinding
import id.stefanusdany.cospace.helper.Helper.showSnackBar

class RecommendationFragment : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 0.0
    private var long = 0.0
    private var distanceWeight = 0
    private var priceWeight = 0
    private var ratingWeight = 0
    private var topsisData = mutableListOf<TopsisEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupView()
        setupViewModel()
        setSpinnerAdapter()
        setupSpinnerAction()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnReset.setOnClickListener {
                binding.spinnerPriceCategory.text = null
                binding.spinnerNearestLocationCategory.text = null
                binding.spinnerRatingCategory.text = null
                lat = 0.0
                long = 0.0
                distanceWeight = 0
                priceWeight = 0
                ratingWeight = 0
                topsisData = mutableListOf<TopsisEntity>()
                isButtonSubmitEnabled()
            }

            btnSubmitRecommendation.setOnClickListener {
                getMyLastLocation()
            }
        }
    }

    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    private fun getTmpCoWorkingSpace() {
        viewModel.getTmpCoWorkingSpace().observe(viewLifecycleOwner) {
            if (it != null) {
                for (i in it.indices) {
                    val a = TopsisEntity(
                        id = it[i].id,
                        name = it[i].name,
                        distance = haversine(-7.9469407,112.6111296, it[i].lat, it[i].long),
                        price = it[i].price,
                        rating = it[i].rating,
                    )
                    topsisData.add(
                        TopsisEntity(
                            id = it[i].id,
                            name = it[i].name,
                            distance = haversine(-7.9469407,112.6111296, it[i].lat, it[i].long),
                            price = it[i].price,
                            rating = it[i].rating,
                        )
                    )
                    println("COSPACE DATA : "+a)
                    Log.e("toop", "lat: $lat + long: $long ", )
                    Log.e("toop", "co: ${topsisData[i].name} distance: ${topsisData[i].distance}", )
                }
                getTOPSISRecommendation(topsisData)

            } else {
                showSnackBar(binding.root, "Terjadi kesalahan saat load lat long")
            }
        }
    }

    private fun temepedaata(): List<TopsisEntity> {
        val tmp = mutableListOf<TopsisEntity>()
        tmp.add(TopsisEntity("1", "Seikophi", 2.69, 29000, 4.7))
        tmp.add(TopsisEntity("2", "Ezo Space", 1.27, 53000, 4.6))
        tmp.add(TopsisEntity("3", "orbits", 2.04, 38000, 4.8))
        tmp.add(TopsisEntity("4", "garten", 1.42, 22000, 4.5))
        tmp.add(TopsisEntity("5", "7seve", 2.37, 65000, 4.6))
        return tmp
    }

    private fun getTOPSISRecommendation(data: List<TopsisEntity>) {
        val instance = Topsis()
        instance.apply {
            setBobot(price = priceWeight, distance = distanceWeight, rating = ratingWeight)
            setDataCoWorkingSpace(data)
            jumlahNilaiAlternatif()
            hasilNormalisasiAlternatif()
            normalisasiTerbobot()
            hasilIdealPositifdanNegatif()
            hasilPerhitunganJarakIdealPositifdanNegatif()
            nilaiPreferensiSetiapAlternatif()
            petakanHasil()
        }
        instance.hasilAkhirRekomendasi.forEach {
            println("DATANYA + $it")
        }
        val bundle = Bundle()
        bundle.putParcelableArrayList(EXTRA_HASHMAP, instance.hasilAkhirRekomendasi)
        bundle.putParcelableArrayList(EXTRA_TOPSIS_DATA, topsisData as ArrayList)
        findNavController().navigate(
            R.id.action_navigation_recommendation_to_resultFragment,
            bundle
        )
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    long = location.longitude
                    getTmpCoWorkingSpace()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Turn on your GPS",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun setSpinnerAdapter() {
        binding.spinnerPriceCategory.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_recommendation)
            )
        )

        binding.spinnerNearestLocationCategory.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_recommendation)
            )
        )

        binding.spinnerRatingCategory.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list,
                resources.getStringArray(R.array.adapter_recommendation)
            )
        )
    }

    private fun isButtonSubmitEnabled() {
        binding.btnSubmitRecommendation.isEnabled =
            binding.spinnerPriceCategory.text.toString()
                .isNotEmpty() && binding.spinnerNearestLocationCategory.text.toString()
                .isNotEmpty() && binding.spinnerRatingCategory.text.toString().isNotEmpty()
    }

    private fun setupSpinnerAction() {
        binding.apply {
            spinnerPriceCategory.setOnItemClickListener { parent, view, position, rowId ->
                val result = parent?.getItemAtPosition(position).toString()
                priceWeight = when (result) {
                    resources.getStringArray(R.array.adapter_recommendation)[0] -> {
                        5
                    }
                    resources.getStringArray(R.array.adapter_recommendation)[1] -> {
                        3
                    }
                    else -> {
                        0
                    }
                }
                isButtonSubmitEnabled()
            }

            spinnerNearestLocationCategory.setOnItemClickListener { parent, view, position, rowId ->
                val result = parent?.getItemAtPosition(position).toString()
                distanceWeight = when (result) {
                    resources.getStringArray(R.array.adapter_recommendation)[0] -> {
                        5
                    }
                    resources.getStringArray(R.array.adapter_recommendation)[1] -> {
                        3
                    }
                    else -> {
                        0
                    }
                }
                isButtonSubmitEnabled()
            }

            spinnerRatingCategory.setOnItemClickListener { parent, view, position, rowId ->
                val result = parent?.getItemAtPosition(position).toString()
                ratingWeight = when (result) {
                    resources.getStringArray(R.array.adapter_recommendation)[0] -> {
                        5
                    }
                    resources.getStringArray(R.array.adapter_recommendation)[1] -> {
                        3
                    }
                    else -> {
                        0
                    }
                }
                isButtonSubmitEnabled()
            }
        }

    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(RecommendationViewModel::class.java)
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val ??1 = toRadians(lat1)
        val ??2 = toRadians(lat2)
        val ???? = toRadians(lat2 - lat1)
        val ???? = toRadians(lon2 - lon1)
        return 2 * radius * kotlin.math.asin(
            kotlin.math.sqrt(
                kotlin.math.sin(???? / 2).pow(2.0) + kotlin.math.sin(???? / 2)
                    .pow(2.0) * kotlin.math.cos(
                    ??1
                ) * kotlin.math.cos(??2)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val radius = 6372.8 // in kilometers
        const val EXTRA_HASHMAP = "extra_hashmap"
        const val EXTRA_TOPSIS_DATA = "extra_topsis_data"
    }
}