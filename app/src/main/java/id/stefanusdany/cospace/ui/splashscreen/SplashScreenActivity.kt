package id.stefanusdany.cospace.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.stefanusdany.cospace.databinding.ActivitySplashScreenBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.IS_LOGIN
import id.stefanusdany.cospace.helper.Helper.UUID
import id.stefanusdany.cospace.ui.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUUID()

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

    private fun setUUID() {
        val sp = getSharedPreferences(Helper.SHARED_PREFERENCE, Context.MODE_PRIVATE) ?: return
        if (!sp.contains(IS_LOGIN)) {
            val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            val id = List(20) { alphabet.random() }.joinToString("")
            with(sp.edit()) {
                putBoolean(IS_LOGIN, true)
                putString(UUID, id)
                apply()
            }
        }

    }
}