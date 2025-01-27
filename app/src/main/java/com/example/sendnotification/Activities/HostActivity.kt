package com.example.sendnotification.Activities


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sendnotification.Adapter.RoomAdapter
import com.example.sendnotification.Modal.Room
import com.example.sendnotification.R
import com.example.sendnotification.utils.NotificationScheduler
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class HostActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var roomsList: MutableList<Room>
    private lateinit var adapter: RoomAdapter
    private lateinit var roomTitleInput: EditText
    private lateinit var createRoomButton: Button
    private lateinit var dateTime: String
    private val hostId = "dummy_host_id" // Replace with the actual logged-in host ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        roomTitleInput = findViewById(R.id.hostHeaderText)
        createRoomButton = findViewById(R.id.createRoomButton)
        val roomsRecyclerView: RecyclerView = findViewById(R.id.hostRoomsRecyclerView)

        roomsList = mutableListOf()
        adapter = RoomAdapter(roomsList, isSubscriber = false)
        roomsRecyclerView.layoutManager = LinearLayoutManager(this)
        roomsRecyclerView.adapter = adapter

        loadRooms()
        createRoomButton.setOnClickListener { showDateTimePicker() }
    }

    private fun loadRooms() {
        db.collection("rooms")
            .whereEqualTo("hostId", hostId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error loading rooms: ${exception.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                roomsList.clear()
                snapshot?.documents?.forEach {
                    roomsList.add(it.toObject(Room::class.java)!!)
                }
                adapter.notifyDataSetChanged()
            }

//        db.collection("rooms")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                roomsList.clear()
//                snapshot.documents.forEach {
//                    roomsList.add(it.toObject(Room::class.java)!!)
//                }
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
//                Log.d("key","${e.message}")
//                e.printStackTrace()
//            }

    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(year, month, day, hour, minute)
                dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
                createRoom()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun createRoom() {
        val title = roomTitleInput.text.toString().trim()
        if (title.isEmpty() || dateTime.isEmpty()) {
            Toast.makeText(this, "Enter valid room details", Toast.LENGTH_SHORT).show()
            return
        }

        val room = Room(
            id = UUID.randomUUID().toString(),
            title = title,
            dateTime = dateTime,
            hostId = hostId,
            participantIds = mutableListOf()
        )

        db.collection("rooms").document(room.id).set(room).addOnSuccessListener {
            Toast.makeText(this, "Room created successfully!", Toast.LENGTH_SHORT).show()
            scheduleNotifications(room)
        }
    }

    private fun scheduleNotifications(room: Room) {
        val calendar = Calendar.getInstance()
        val roomTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(room.dateTime) ?: return

        // Schedule host notifications
        calendar.time = roomTime
        calendar.add(Calendar.MINUTE, -10)
        NotificationScheduler.scheduleNotification(
            this, "Prepare Room", "Prepare to host '${room.title}'", calendar.timeInMillis, room.id
        )

        calendar.time = roomTime
        calendar.add(Calendar.MINUTE, -3)
        NotificationScheduler.scheduleNotification(
            this, "Start Room", "Time to start your room '${room.title}'", calendar.timeInMillis, room.id
        )

        // Notify all subscribers
        notifySubscribers(room)
    }

    private fun notifySubscribers(room: Room) {
        // Assuming a list of followers is stored in the database
        db.collection("users").whereEqualTo("followingHostId", hostId).get().addOnSuccessListener { snapshot ->
            snapshot.documents.forEach { doc ->
                val userId = doc.id
                val message = "New Room '${room.title}' by Host on ${room.dateTime}"
                // Use Firebase Cloud Messaging (FCM) to notify the subscriber
                Toast.makeText(this, "Notified user: $userId", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
