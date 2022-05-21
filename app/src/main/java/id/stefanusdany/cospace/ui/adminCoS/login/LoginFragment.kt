package id.stefanusdany.cospace.ui.adminCoS.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.ViewModelFactory
import id.stefanusdany.cospace.databinding.FragmentLoginBinding
import id.stefanusdany.cospace.helper.Helper.showSnackBar
import id.stefanusdany.cospace.helper.Helper.visibility

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = factory.create(LoginViewModel::class.java)
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLoginAdmin.setOnClickListener {
            //delete soon
            binding.etUsername.setText("ruangperintis")
            binding.etPassword.setText("cospace123")
            binding.progressBar.visibility(true)
            when {
                binding.etUsername.text.toString().trim().isEmpty() -> {
                    binding.etUsername.error = getString(R.string.error_username)
                    binding.etUsername.requestFocus()
                    binding.progressBar.visibility(false)
                }

                binding.etPassword.text.toString().trim().isEmpty() -> {
                    binding.etPassword.error = getString(R.string.error_password)
                    binding.etPassword.requestFocus()
                    binding.progressBar.visibility(false)
                }

                else -> {
                    viewModel.getAuthentication(
                        binding.etUsername.text.toString().trim(),
                        binding.etPassword.text.toString()
                    ).observe(viewLifecycleOwner) {
                        if (it.username.isNotEmpty() && it.password.isNotEmpty()) {
                            binding.progressBar.visibility(false)
                            showSnackBar(binding.root, getString(R.string.success_login))
                            val toHomepageAdmin =
                                LoginFragmentDirections.actionLoginFragmentToHomepageAdminFragment(
                                    it
                                )
                            findNavController().navigate(toHomepageAdmin)
                        } else {
                            showSnackBar(
                                binding.root,
                                getString(R.string.error_user_password_is_not_matched)
                            )
                            binding.progressBar.visibility(false)
                        }
                    }
                }

            }
        }
    }
}