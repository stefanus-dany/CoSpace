package id.stefanusdany.cospace.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
    const val SHARED_PREFERENCE = "sp"
    const val IS_LOGIN = "is_login"
    const val UUID = "uuid"
    const val NAME = "name"
    const val EMAIL_COSPACE = "cospace.app@gmail.com"
    const val PASS_COSPACE = "CoSpace123"

    private const val NETWORK_NOT_CONNECTED = -1
    private const val NETWORK_CONNECTED = 1

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

    fun isConnected(context: Context): Boolean {
        val status = getConnectivityStatus(context)
        return status == NETWORK_CONNECTED
    }

    private fun getConnectivityStatus(context: Context): Int {
        val cm = getConnectionManager(context)!!
        val activeNetwork = getNetworkInfo(cm)
        if (null != activeNetwork) {
            println("active network: ${activeNetwork.type}")
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return NETWORK_CONNECTED

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return NETWORK_CONNECTED
        }
        return NETWORK_NOT_CONNECTED
    }

    private fun getConnectionManager(context: Context): ConnectivityManager? {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun getNetworkInfo(connectivityManager: ConnectivityManager): NetworkInfo? {
        return connectivityManager.activeNetworkInfo
    }
}