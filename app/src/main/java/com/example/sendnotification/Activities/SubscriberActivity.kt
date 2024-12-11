package com.example.sendnotification.Activities

import com.example.sendnotification.Adapter.RoomAdapter
import com.example.sendnotification.Modal.Room
import com.example.sendnotification.R


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SubscriberActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var roomsList: MutableList<Room>
    private lateinit var adapter: RoomAdapter
    private val userId = "dummy_user_id" // Replace with actual subscriber ID
    private val userName = "Subscriber Name" // Replace with actual subscriber name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscriber)

        val roomsRecyclerView: RecyclerView = findViewById(R.id.roomsRecyclerView)

        roomsList = mutableListOf()
        adapter = RoomAdapter(roomsList, isSubscriber = true) { roomId ->
            joinRoom(roomId)
        }
        roomsRecyclerView.layoutManager = LinearLayoutManager(this)
        roomsRecyclerView.adapter = adapter

        loadRooms()
    }

    private fun loadRooms() {
        db.collection("rooms")
            .addSnapshotListener { snapshot, _ ->
                roomsList.clear()
                snapshot?.documents?.forEach {
                    roomsList.add(it.toObject(Room::class.java)!!)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun joinRoom(roomId: String) {
        db.collection("rooms").document(roomId).get().addOnSuccessListener { document ->
            val room = document.toObject(Room::class.java)
            if (room != null) {
                if (!room.participantIds.contains(userId)) {
                    room.participantIds.add(userId)
                    db.collection("rooms").document(roomId).set(room).addOnSuccessListener {
                        Toast.makeText(this, "Request to join room sent!", Toast.LENGTH_SHORT).show()
                        notifyHost(room.hostId, room.title, userName)
                    }
                } else {
                    Toast.makeText(this, "You have already joined this room.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun notifyHost(hostId: String, roomTitle: String, userName: String) {
        val message = "$userName has requested to join your room '$roomTitle'."
        // Notify host using FCM or your notification system
        Toast.makeText(this, "Host notified: $message", Toast.LENGTH_SHORT).show()
    }
}
