package id.stefanusdany.cospace.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.stefanusdany.cospace.data.entity.BookingEntity
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.data.entity.ImagesEntity
import id.stefanusdany.cospace.data.entity.PostEntity
import id.stefanusdany.cospace.data.entity.TmpEntity
import id.stefanusdany.cospace.data.entity.WorkingHourEntity
import id.stefanusdany.cospace.helper.Helper.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(private val database: FirebaseDatabase, private val storage: FirebaseStorage) {

    fun getAllCoWorkingSpace(): LiveData<List<CoWorkingSpaceEntity>> {
        val data = MutableLiveData<List<CoWorkingSpaceEntity>>()
        val tmpData = mutableListOf<CoWorkingSpaceEntity>()
        val booking = mutableListOf<BookingEntity>()
        val facility = mutableListOf<FacilityEntity>()
        val images = mutableListOf<ImagesEntity>()
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
                                booking.clear()
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
//
                        //TODO GET FACILITY
                        val getFacility = cosId.child("facility")
                        getFacility.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                facility.clear()
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
                                images.clear()
                                for (dataSnapshotImages: DataSnapshot in snapshot.children) {
                                    val valueImages = dataSnapshotImages.getValue(ImagesEntity::class.java)

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
                                post.clear()
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
                                workingHour.clear()
                                for (dataSnapshotWorkingHour: DataSnapshot in snapshot.children) {
                                    val valueWorkingHour =
                                        dataSnapshotWorkingHour.getValue(WorkingHourEntity::class.java)

                                    if (valueWorkingHour != null) {
                                        workingHour.add(valueWorkingHour)
                                    }


                                }

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

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
                                    image = valueCoWorkingSpace.image,
                                    post = post,
                                    workingHour = workingHour,
                                    images = images,
                                    booking = booking,
                                    facility = facility
                                )
                            )
                        }

                    }
                    data.value = tmpData

                    Log.d(TAG, "Value is: ${data.value}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }

        return data
    }

    fun uploadPaymentSlip(
        uriPaymentSlip: Uri?,
        idCoS: String,
        bookingId: String
    ): LiveData<Int> {
        val url = MutableLiveData<Int>()
        CoroutineScope(Dispatchers.IO).launch {
            val tmp: StorageReference =
                storage.reference.child("PaymentSlip/$idCoS/$bookingId.jpg")

            if (uriPaymentSlip != null) {
                tmp.putFile(uriPaymentSlip).addOnSuccessListener {
                    //getImageUrl
                    tmp.downloadUrl.addOnSuccessListener {
                        url.value = 1
                        val uploadToDatabase =
                            database.getReference("coworking_space").child(idCoS).child("booking")
                                .child(bookingId)
                                .child("paymentSlip")
                        uploadToDatabase.setValue(it.toString())
                    }

                }.addOnFailureListener {
                    url.value = 0
                }
            }
        }
        return url
    }

    fun uploadBooking(dataBooking: BookingEntity, idCoS: String, bookingId: String): LiveData<Int> {
        val code = MutableLiveData<Int>()
        CoroutineScope(Dispatchers.IO).launch {
            val uploadToDatabase =
                database.getReference("coworking_space").child(idCoS).child("booking")
                    .child(bookingId)
            uploadToDatabase.setValue(dataBooking).addOnSuccessListener {
                code.value = 1
            }.addOnFailureListener {
                code.value = 0
            }
        }
        return code
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
            database: FirebaseDatabase,
            storage: FirebaseStorage
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(database, storage)
            }.also { instance = it }
    }
}