package id.stefanusdany.cospace.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.data.entity.PostEntity
import id.stefanusdany.cospace.data.entity.TmpEntity
import id.stefanusdany.cospace.data.entity.WorkingHourEntity
import id.stefanusdany.cospace.helper.Helper.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(private val database: FirebaseDatabase) {

    fun getAllCoWorkingSpace(): LiveData<List<CoWorkingSpaceEntity>> {
        val data = MutableLiveData<List<CoWorkingSpaceEntity>>()
        val tmpData = mutableListOf<CoWorkingSpaceEntity>()
        val booking = mutableListOf<BookingEntity>()
        val facility = mutableListOf<FacilityEntity>()
        val images = mutableListOf<String>()
        val post = mutableListOf<PostEntity>()
        val workingHour = mutableListOf<WorkingHourEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            val getCoWorkingSpace = database.getReference("coworking_space")

            //TODO GET COWORKING SPACE
            getCoWorkingSpace.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshotCoWorkingSpace: DataSnapshot in snapshot.children) {
                        val valueCoWorkingSpace =
                            dataSnapshotCoWorkingSpace.getValue(TmpEntity::class.java)
                        val cosId = getCoWorkingSpace.child(valueCoWorkingSpace?.id.toString())

                        //TODO GET BOOKING
                        val getBooking = cosId.child("booking")
                        getBooking.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshotBooking: DataSnapshot in snapshot.children) {
                                    val valueBooking =
                                        dataSnapshotBooking.getValue(BookingEntity::class.java)
                                    if (valueBooking != null) {
                                        booking.add(valueBooking)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                        //TODO GET FACILITY
                        val getFacility = cosId.child("facility")
                        getFacility.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshotFacility: DataSnapshot in snapshot.children) {
                                    val valueFacility =
                                        dataSnapshotFacility.getValue(FacilityEntity::class.java)

                                    if (valueFacility != null) {
                                        facility.add(valueFacility)
                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                        //TODO GET IMAGES
                        val getImages = cosId.child("images")
                        getImages.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshotImages: DataSnapshot in snapshot.children) {
                                    val valueImages = dataSnapshotImages.getValue<String>()

                                    if (valueImages != null) {
                                        images.add(valueImages)
                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                        //TODO GET POST
                        val getPost = cosId.child("post")
                        getPost.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshotPost: DataSnapshot in snapshot.children) {
                                    val valuePost =
                                        dataSnapshotPost.getValue(PostEntity::class.java)


                                    if (valuePost != null) {
                                        post.add(valuePost)
                                    }


                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                        //TODO GET WORKING HOUR
                        val getWorkingHour = cosId.child("workingHour")
                        getWorkingHour.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshotWorkingHour: DataSnapshot in snapshot.children) {
                                    val valueWorkingHour =
                                        dataSnapshotWorkingHour.getValue(WorkingHourEntity::class.java)

                                    if (valueWorkingHour != null) {
                                        workingHour.add(valueWorkingHour)
                                    }


                                }
                                if (valueCoWorkingSpace != null) {
                                    tmpData.add(
                                        CoWorkingSpaceEntity(
                                            id = valueCoWorkingSpace.id,
                                            name = valueCoWorkingSpace.name,
                                            address = valueCoWorkingSpace.address,
                                            capacity = valueCoWorkingSpace.capacity,
                                            googleMaps = valueCoWorkingSpace.googleMaps,
                                            lat = valueCoWorkingSpace.lat,
                                            long = valueCoWorkingSpace.long,
                                            price = valueCoWorkingSpace.price,
                                            post, workingHour, images, booking, facility
                                        )
                                    )
                                }
                                data.value = tmpData

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                    }

                    Log.d(TAG, "Value is: ${data.value}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }

        return data
    }

//    fun register(
//        name: String,
//        email: String,
//        password: String
//    ): LiveData<Result<RegisterResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.register(BodyRegister(name, email, password))
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            Log.e(TAG, "onFailure: ${e.message.toString()}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }
//
//    fun uploadStory(
//        token: String,
//        file: MultipartBody.Part,
//        description: RequestBody,
//        lat: RequestBody,
//        lon: RequestBody
//    ): LiveData<Result<FileUploadResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.uploadImage(token, file, description, lat, lon)
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            Log.e(TAG, "onFailure: ${e.message.toString()}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            database: FirebaseDatabase
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(database)
            }.also { instance = it }
    }
}