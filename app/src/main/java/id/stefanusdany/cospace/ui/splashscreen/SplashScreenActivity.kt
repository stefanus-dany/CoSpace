package id.stefanusdany.cospace.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.stefanusdany.cospace.databinding.ActivitySplashScreenBinding
import id.stefanusdany.cospace.helper.Helper
import id.stefanusdany.cospace.helper.Helper.IS_LOGIN
import id.stefanusdany.cospace.helper.Helper.NAME
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

        binding.ivSplash.apply {
            alpha = Helper.START_ALPHA_SPLASH
            animate().setDuration(Helper.DURATION_SPLASH).alpha(
                Helper.END_ALPHA_SPLASH
            )
                .withEndAction {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    setNameAndUUID()
                }
        }
    }

    private fun setNameAndUUID() {
        val sp = getSharedPreferences(Helper.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        if (!sp.contains(IS_LOGIN)) {
            val etName = EditText(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            }
            val dialog = AlertDialog.Builder(this).setTitle("Insert your name!")
                .setView(etName)
                .setCancelable(false)
                .setPositiveButton("Confirm", null)
                .show()

            val btnConfirm = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnConfirm.setOnClickListener {
                if (etName.text.toString().trim().isNotEmpty()) {
                    val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                    val id = List(20) { alphabet.random() }.joinToString("")
                    with(sp.edit()) {
                        putBoolean(IS_LOGIN, true)
                        putString(UUID, id)
                        putString(NAME, etName.text.toString())
                        apply()
                    }
                    moveToHomepage()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please insert your name!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            moveToHomepage()
        }
    }

    private fun moveToHomepage() {
        val move = Intent(this@SplashScreenActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(move)
    }
}