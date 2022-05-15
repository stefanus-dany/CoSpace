package id.stefanusdany.cospace.di

import android.content.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.stefanusdany.cospace.data.Repository

object Injection {

    fun provideRepository(context: Context): Repository {
        val database = Firebase.database
        return Repository.getInstance(database)
    }
}