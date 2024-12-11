package com.example.sendnotification.Repositories

// NotificationRepository.kt
import com.example.sendnotification.Interfaces.ApiService
import com.example.sendnotification.Modal.ApiResponse
import com.example.sendnotification.Modal.SendData
import com.example.sendnotification.Modal.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(private val apiService: ApiService) {

    suspend fun sendNotificatio(sendData: SendData) {
        withContext(Dispatchers.IO) {
            apiService.sendNotification(sendData)
        }
    }

    suspend fun getUserDetail(): ApiResponse {
        return withContext(Dispatchers.IO) {
            apiService.getUserDetails()
        }
    }


    // Mocked data for user details
    private val userDetailsList = mutableListOf(
        UserDetail(1, "John Doe", "token123"),
        UserDetail(2, "Jane Smith", "token456"),
        UserDetail(3, "Alice Brown", "token789"),
        UserDetail(4, "Sai", "token9573"),
        UserDetail(5, "Bad", "token857")



    )

    // Mock sendNotification function
    suspend fun sendNotification2(sendData: SendData) {
        withContext(Dispatchers.IO) {
            // Simulate sending notification (you could log or print here if needed)
            println("Notification sent: $sendData  ")
        }
    }

    // Mock getUserDetails function
    suspend fun getUserDetails2(): ApiResponse {
        return withContext(Dispatchers.IO) {
            ApiResponse(
                host = userDetailsList[0], // The first user is treated as the host
                hostSubscribers = userDetailsList
            )
        }
    }
}
