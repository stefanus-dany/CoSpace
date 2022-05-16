package id.stefanusdany.cospace.helper

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView

object Helper {

    //splashscreen
    const val START_ALPHA_SPLASH = 0f
    const val END_ALPHA_SPLASH = 1f
    const val DURATION_SPLASH = 1500L

    //log
    const val TAG = "log"

    fun CircleImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(300, 300))
            .centerCrop()
            .into(this)
    }

    fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(300, 300))
            .centerCrop()
            .into(this)
    }

    fun showSnackBar(view: View, text: String) {
        Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun View.visibility(value: Boolean) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
}