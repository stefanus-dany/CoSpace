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
import id.stefanusdany.cospace.data.entity.ChatEntity
import id.stefanusdany.cospace.data.entity.CoWorkingSpaceEntity
import id.stefanusdany.cospace.data.entity.FacilityEntity
import id.stefanusdany.cospace.data.entity.IdChatEntity
import id.stefanusdany.cospace.data.entity.ImagesEntity
import id.stefanusdany.cospace.data.entity.LoginEntity
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
                                    val valueImages =
                                        dataSnapshotImages.getValue(ImagesEntity::class.java)

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
                                    rating = valueCoWorkingSpace.rating,
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

    fun getTmpCoWorkingSpace(): LiveData<List<TmpEntity>> {
        val data = MutableLiveData<List<TmpEntity>>()
        val tmpData = mutableListOf<TmpEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            val getCoWorkingSpace = database.getReference("coworking_space")

            //TODO GET COWORKING SPACE
            getCoWorkingSpace.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshotCoWorkingSpace: DataSnapshot in snapshot.children) {
                        val valueCoWorkingSpace =
                            dataSnapshotCoWorkingSpace.getValue(TmpEntity::class.java)
                        if (valueCoWorkingSpace != null) {
                            tmpData.add(valueCoWorkingSpace)
                        }
                    }
                    data.value = tmpData
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        return data
    }

    fun getAllChatsUser(uuid: String): LiveData<List<IdChatEntity>> {
        val data = MutableLiveData<List<IdChatEntity>>()
        val tmpDataIDChat = mutableListOf<IdChatEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getAllUserIdChat = database.getReference("user").child(uuid)
            getAllUserIdChat.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val value = dataSnapshot.getValue(IdChatEntity::class.java)
                        if (value != null) {
                            tmpDataIDChat.add(value)
                        }
                    }
                    data.value = tmpDataIDChat
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }

            })
        }
        return data
    }

    fun getAllChatsCoSpace(idCoSpace: String): LiveData<List<IdChatEntity>> {
        val data = MutableLiveData<List<IdChatEntity>>()
        val tmpDataIDChat = mutableListOf<IdChatEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getAllUserIdChat = database.getReference("chatCoSpace").child(idCoSpace)
            getAllUserIdChat.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val value = dataSnapshot.getValue(IdChatEntity::class.java)
                        if (value != null) {
                            tmpDataIDChat.add(value)
                        }
                    }
                    data.value = tmpDataIDChat
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }

            })
        }
        return data
    }

    fun getAllDetailChat(idChat: String): LiveData<List<ChatEntity>> {
        val data = MutableLiveData<List<ChatEntity>>()
        val tmpDataAllDetailChat = mutableListOf<ChatEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getAllUserIdChat = database.getReference("chat").child(idChat)

            getAllUserIdChat.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value =
                            dataSnapshot.getValue(ChatEntity::class.java)
                        if (value != null) {
                            tmpDataAllDetailChat.add(value)
                        }
                    }
                    val distinct = tmpDataAllDetailChat.distinct()
                    data.value = distinct
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun getAuthentication(username: String, password: String): LiveData<LoginEntity> {
        val data = MutableLiveData<LoginEntity>()
        var tmpGetAuth = LoginEntity()

        CoroutineScope(Dispatchers.IO).launch {
            val getAllUserIdChat = database.getReference("auth")

            getAllUserIdChat.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value =
                            dataSnapshot.getValue(LoginEntity::class.java)
                        if (value != null) {
                            if (value.username == username && value.password == password) {
                                tmpGetAuth = value
                            }
                        }
                    }
                    data.value = tmpGetAuth
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun getAllBookingConfirmation(idCoSpace: String): LiveData<List<BookingEntity>> {
        val data = MutableLiveData<List<BookingEntity>>()
        val tmpData = mutableListOf<BookingEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getAllUserIdChat =
                database.getReference("coworking_space").child(idCoSpace).child("booking")

            getAllUserIdChat.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value =
                            dataSnapshot.getValue(BookingEntity::class.java)
                        if (value != null) {
                            tmpData.add(value)
                        }
                    }
                    data.value = tmpData
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun sendAcceptedBooking(idCoSpace: String, bookingData: BookingEntity): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        data.value = false
        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("successfulBooking")
                    .child(bookingData.id)

            getUrl.setValue(bookingData).addOnSuccessListener {
                val deleteFromBooking =
                    database.getReference("coworking_space").child(idCoSpace).child("booking")
                        .child(bookingData.id)
                deleteFromBooking.removeValue().addOnSuccessListener {
                    data.value = true
                }
            }

        }
        return data
    }

    fun deleteAcceptedBooking(idCoSpace: String, bookingData: BookingEntity): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        data.value = false
        CoroutineScope(Dispatchers.IO).launch {
            val deleteFromBooking =
                database.getReference("coworking_space").child(idCoSpace).child("booking")
                    .child(bookingData.id)
            deleteFromBooking.removeValue().addOnSuccessListener {
                data.value = true
            }
        }
        return data
    }

    fun getAllSuccessfulBooking(idCoSpace: String): LiveData<List<BookingEntity>> {
        val data = MutableLiveData<List<BookingEntity>>()
        val tmpData = mutableListOf<BookingEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("successfulBooking")

            getUrl.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value =
                            dataSnapshot.getValue(BookingEntity::class.java)
                        if (value != null) {
                            tmpData.add(value)
                        }
                    }
                    data.value = tmpData
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun getAllFacility(idCoSpace: String): LiveData<List<FacilityEntity>> {
        val data = MutableLiveData<List<FacilityEntity>>()
        val tmpData = mutableListOf<FacilityEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("facility")

            getUrl.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value =
                            dataSnapshot.getValue(FacilityEntity::class.java)
                        if (value != null) {
                            tmpData.add(value)
                        }
                    }
                    data.value = tmpData
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun deleteFacility(idCoSpace: String, idFacility: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("facility")
                    .child(idFacility)
            getUrl.removeValue()
        }
    }

    fun addFacility(idCoSpace: String, facility: FacilityEntity): LiveData<Boolean> {
        val value = MutableLiveData(false)
        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("facility")
                    .child(facility.id)
            getUrl.setValue(facility).addOnSuccessListener {
                value.value = true
            }
        }
        return value
    }

    fun getAllWorkingHour(idCoSpace: String): LiveData<List<WorkingHourEntity>> {
        val data = MutableLiveData<List<WorkingHourEntity>>()
        val tmpData = mutableListOf<WorkingHourEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("workingHour")

            getUrl.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value =
                            dataSnapshot.getValue(WorkingHourEntity::class.java)
                        if (value != null) {
                            tmpData.add(value)
                        }
                    }
                    data.value = tmpData
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun addWorkingHour(idCoSpace: String, workingHour: WorkingHourEntity): LiveData<Boolean> {
        val value = MutableLiveData(false)
        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("workingHour")
                    .child(workingHour.id)
            getUrl.setValue(workingHour).addOnSuccessListener {
                value.value = true
            }
        }
        return value
    }

    fun deleteWorkingHour(idCoSpace: String, workingHour: WorkingHourEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace).child("workingHour")
                    .child(workingHour.id)
            getUrl.removeValue()
        }
    }

    fun getCoWorkingSpaceDetail(idCoSpace: String): LiveData<List<Any>> {
        val data = MutableLiveData<List<Any>>()
        val tmpData = mutableListOf<Any>()

        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace)

            getUrl.child("price").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue(Int::class.java)
                    if (value != null) {
                        tmpData.add(value)
                    }
                    getUrl.child("capacity")
                        .addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val value2 =
                                    snapshot.getValue(Int::class.java)
                                if (value2 != null) {
                                    tmpData.add(value2)
                                }
                                getUrl.child("address")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val value3 =
                                                snapshot.getValue(String::class.java)
                                            if (value3 != null) {
                                                tmpData.add(value3)
                                            }
                                            data.value = tmpData
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.e(TAG, "onCancelled: ${error.message}")
                                        }
                                    })
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, "onCancelled: ${error.message}")
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return data
    }

    fun saveCoWorkingSpaceDetail(idCoSpace: String, data: List<Any>): LiveData<Boolean> {
        val value = MutableLiveData(false)
        CoroutineScope(Dispatchers.IO).launch {
            val getUrl =
                database.getReference("coworking_space").child(idCoSpace)

            getUrl.child("price").setValue(data[0]).addOnSuccessListener {
                getUrl.child("capacity").setValue(data[1]).addOnSuccessListener {
                    getUrl.child("address").setValue(data[2]).addOnSuccessListener {
                        value.value = true
                    }
                }
            }
        }
        return value
    }

    fun sendChatToDatabase(data: ChatEntity, idDetailChat: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val sendChat = database.getReference("chat").child(idDetailChat).child(data.id)
            sendChat.setValue(data)
        }
    }

    fun isExistChatWithCoSpace(idUser: String, idCoSpace: String): LiveData<IdChatEntity> {
        var tmpData = IdChatEntity()
        val data = MutableLiveData(tmpData)
        CoroutineScope(Dispatchers.IO).launch {
            val url =
                database.getReference("user").child(idUser).child(idCoSpace)
            url.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(IdChatEntity::class.java)
                    if (value != null){
                        tmpData = value
                    }
                    data.value = tmpData
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }

            })

        }
        return data
    }

    fun creatingChats(
        idChatEntityUser: IdChatEntity,
        idChatEntityCoSpace: IdChatEntity
    ): LiveData<Boolean> {
        val data = MutableLiveData(false)
        CoroutineScope(Dispatchers.IO).launch {
            val createUserChat = database.getReference("user").child(idChatEntityCoSpace.id)
                .child(idChatEntityUser.id)
            createUserChat.setValue(idChatEntityUser).addOnSuccessListener {
                val createCoWorkingChat =
                    database.getReference("chatCoSpace").child(idChatEntityUser.id)
                        .child(idChatEntityCoSpace.id)
                createCoWorkingChat.setValue(idChatEntityCoSpace).addOnSuccessListener {
                    data.value = true
                }
            }
        }
        return data
    }

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