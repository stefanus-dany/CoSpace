package id.stefanusdany.cospace.ui.adminCoS.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.databinding.FragmentHomepageAdminBinding

class HomepageAdminFragment : Fragment() {

    private var _binding: FragmentHomepageAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataBundle: HomepageAdminFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepageAdminBinding.inflate(inflater, container, false)
        dataBundle = HomepageAdminFragmentArgs.fromBundle(arguments as Bundle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupAction()
    }

    private fun setupView() {
        binding.tvWelcomeCoSpace.text = getString(R.string.label_welcome, dataBundle.dataLogin.name)
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun setupAction() {
        binding.apply {
            btnBookingConfirmation.setOnClickListener {
                val navigate = HomepageAdminFragmentDirections.actionHomepageAdminFragmentToBookingConfirmationFragment(dataBundle.dataLogin)
                findNavController().navigate(navigate)
            }

            btnSuccessfulBooking.setOnClickListener {
                val navigate = HomepageAdminFragmentDirections.actionHomepageAdminFragmentToSuccessfulBookingFragment(dataBundle.dataLogin)
                findNavController().navigate(navigate)
            }

            btnCoSDetail.setOnClickListener {
                val navigate = HomepageAdminFragmentDirections.actionHomepageAdminFragmentToCoSDetailFragment(dataBundle.dataLogin)
                findNavController().navigate(navigate)
            }

            btnCoSFacility.setOnClickListener {
                val navigate = HomepageAdminFragmentDirections.actionHomepageAdminFragmentToCoSFacilityFragment(dataBundle.dataLogin)
                findNavController().navigate(navigate)
            }

            btnLiveChat.setOnClickListener {
                val navigate = HomepageAdminFragmentDirections.actionHomepageAdminFragmentToAdminChatFragment(dataBundle.dataLogin)
                findNavController().navigate(navigate)
            }

            btnLogoutAdmin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}