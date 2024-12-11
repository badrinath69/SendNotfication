package com.example.sendnotification.utils

// Provide Retrofit instance and ApiService
import com.example.sendnotification.Interfaces.ApiService
import com.example.sendnotification.Repositories.NotificationRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://example.com/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// Manual DI
val repository = NotificationRepository(RetrofitInstance.apiService)
val viewModelFactory = NotificationViewModelFactory(repository)
