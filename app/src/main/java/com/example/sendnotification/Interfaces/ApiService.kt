package com.example.sendnotification.Interfaces

// ApiService.kt
import com.example.sendnotification.Modal.ApiResponse
import com.example.sendnotification.Modal.SendData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("sendNotification")
    suspend fun sendNotification(@Body sendData: SendData): Unit

    @GET("getUserDetails")
    suspend fun getUserDetails(): ApiResponse
}
