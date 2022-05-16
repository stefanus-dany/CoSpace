package id.stefanusdany.cospace.di

import android.content.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import id.stefanusdany.cospace.data.Repository

object Injection {

    fun provideRepository(context: Context): Repository {
        val database = Firebase.database
        val storage = FirebaseStorage.getInstance()
        return Repository.getInstance(database, storage)
    }
}