package id.stefanusdany.cospace.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.stefanusdany.cospace.databinding.ActivitySplashScreenBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.ui.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.ivSplash.apply {
            alpha = Helper.START_ALPHA_SPLASH
            animate().setDuration(Helper.DURATION_SPLASH).alpha(
                Helper.END_ALPHA_SPLASH
            )
                .withEndAction {
                    val move = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(move)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
        }
    }
}