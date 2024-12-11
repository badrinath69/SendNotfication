package com.example.sendnotification.Activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sendnotification.Adapter.UserDetailAdapter
import com.example.sendnotification.Modal.SendData
import com.example.sendnotification.Modal.UserDetail
import com.example.sendnotification.R
import com.example.sendnotification.ViewModel.NotificationViewModel
import com.example.sendnotification.databinding.ActivityNotificationBinding
import com.example.sendnotification.utils.NotificationViewModelFactory
import com.example.sendnotification.utils.viewModelFactory

class NotificationActivity : AppCompatActivity() {
//    private val viewModel: NotificationViewModel by viewModels {
//        NotificationViewModelFactory(MockNotificationRepository())
//    }
    private val viewModel: NotificationViewModel by viewModels { viewModelFactory }
    private lateinit var adapter: UserDetailAdapter
    private var userDetailsList: List<UserDetail> = emptyList()


    private lateinit var views: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(views.root)

        createNotificationChannel()

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe user details
//        viewModel.userDetails2.observe(this, Observer { response ->
//            if (response != null) {
//                adapter = UserDetailAdapter(response.hostSubscribers) { userDetail, ->
//                    // Trigger notification for clicked user
//                    val notificationData = SendData(
//                        token = userDetail.deviceToken,
//                        title = "User ID: ${userDetail.id}",
//                        message = "Notification sent to ${userDetail.name}"
//                    )
//                    viewModel.sendNotification2(notificationData,position)
//                }
//                recyclerView.adapter = adapter
//            } else {
//                Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
//            }
//        })

        viewModel.userDetails2.observe(this, Observer { response ->
            if (response != null) {
                userDetailsList = response.hostSubscribers
                Log.d("bbb","$userDetailsList")
                adapter = UserDetailAdapter(response.hostSubscribers) { clickedUser ->
                    val position = userDetailsList.indexOf(clickedUser)
                    if (position != -1) {
                        // Trigger notification
                        val notificationData = SendData(
                            token = clickedUser.deviceToken,
                            title = "User ID: ${clickedUser.id}",
                            message = "Notification sent to ${clickedUser.name}"
                        )
                        viewModel.sendNotification2(notificationData, position)
                    }
                }
                recyclerView.adapter = adapter
            } else {
                Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
            }
        })

        // Observe notification status
        viewModel.isNotificationSent2.observe(this, Observer { success ->
            val position = viewModel.lastClickedPosition.value ?: -1

            Log.d("bbb","${userDetailsList[position]}")

            if (success) {
                Toast.makeText(this, "Notification sent successfully at $position", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to send notification at $position", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch user details
        viewModel.fetchUserDetails2()







        // Observe data
//        viewModel.userDetails.observe(this, Observer { response ->
//            if (response != null) {
//                adapter.submitList(response.hostSubscribers)
//            } else {
//                Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        viewModel.isNotificationSent.observe(this, Observer { success ->
//            if (success) {
//                Toast.makeText(this, "Notification sent successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Failed to send notification", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        // Fetch User Details
//        viewModel.fetchUserDetails()

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel_id"
            val channelName = "General Notifications"
            val channelDescription = "Used for general notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}