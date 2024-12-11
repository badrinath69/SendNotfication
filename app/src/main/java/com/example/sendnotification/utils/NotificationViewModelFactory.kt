package com.example.sendnotification.utils

// NotificationViewModelFactory.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sendnotification.Repositories.NotificationRepository
import com.example.sendnotification.ViewModel.NotificationViewModel

class NotificationViewModelFactory(private val repository: NotificationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
