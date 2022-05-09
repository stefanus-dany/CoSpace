package id.stefanusdany.cospace.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.databinding.FragmentRecommendationBinding

class RecommendationFragment : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(RecommendationViewModel::class.java)

        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_input_bobot)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}