package com.example.sendnotification.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sendnotification.Modal.User
import com.example.sendnotification.Repositories.UserRepository

class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    val users = MutableLiveData<List<User>>()
    val isLoading = MutableLiveData<Boolean>()

    fun addUser(user: User, onComplete: (Boolean) -> Unit) {
        isLoading.value = true
        repository.addUser(user) { success ->
            isLoading.value = false
            onComplete(success)
        }
    }

    fun fetchUsers() {
        isLoading.value = true
        repository.getUsers { userList ->
            isLoading.value = false
            users.value = userList
        }
    }
}
