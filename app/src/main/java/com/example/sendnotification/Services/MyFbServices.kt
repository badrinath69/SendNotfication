package com.example.sendnotification.Services

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.remoteMessage

class MyFbServices:FirebaseMessagingService() {
    private  var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onMessageReceived(message: RemoteMessage) {
            Looper.prepare()
        Handler().post{
            Toast.makeText(baseContext, message.notification?.title,Toast.LENGTH_SHORT).show()
        }
        Looper.loop()
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val userId = firebaseAuth.currentUser?.uid ?: ""

        if (userId.isNotEmpty()) {
            FirebaseDatabase.getInstance().getReference("users").child(userId).child("fcmToken").setValue(token)
            Log.d("FCM", "Updated refreshed token for user: $userId")
        }
    }

}