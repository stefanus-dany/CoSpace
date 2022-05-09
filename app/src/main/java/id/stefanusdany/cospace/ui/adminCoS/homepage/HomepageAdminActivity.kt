package id.stefanusdany.cospace.ui.adminCoS.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.stefanusdany.cospace.R
import id.stefanusdany.cospace.databinding.ActivityHomepageAdminBinding

class HomepageAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}