package com.example.sendnotification.ViewModel

// NotificationViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sendnotification.Modal.ApiResponse
import com.example.sendnotification.Modal.SendData
import com.example.sendnotification.Repositories.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

//    private val _userDetails = MutableLiveData<ApiResponse>()
//    val userDetails: LiveData<ApiResponse> get() = _userDetails
//
//    private val _isNotificationSent = MutableLiveData<Boolean>()
//    val isNotificationSent: LiveData<Boolean> get() = _isNotificationSent


    private val _userDetails2 = MutableLiveData<ApiResponse>()
    val userDetails2: LiveData<ApiResponse> get() = _userDetails2

    private val _isNotificationSent2 = MutableLiveData<Boolean>()
    val isNotificationSent2: LiveData<Boolean> get() = _isNotificationSent2

    private val _lastClickedPosition2 = MutableLiveData<Int>()
    val lastClickedPosition: LiveData<Int> get() = _lastClickedPosition2



//    fun sendNotification(sendData: SendData) {
//        viewModelScope.launch {
//            try {
//                repository.sendNotificatio(sendData)
//                _isNotificationSent.value = true
//            } catch (e: Exception) {
//                _isNotificationSent.value = false
//            }
//        }
//    }
//
//    fun fetchUserDetails() {
//        viewModelScope.launch {
//            try {
//                val response = repository.getUserDetail()
//                _userDetails.value = response
//            } catch (e: Exception) {
//                _userDetails.value = null
//            }
//        }
//    }




    fun sendNotification2(sendData: SendData,position:Int) {
        viewModelScope.launch {
            try {
                repository.sendNotification2(sendData)
                _lastClickedPosition2.value=position
                _isNotificationSent2.value = true
            } catch (e: Exception) {
                _isNotificationSent2.value = false
            }
        }
    }

    fun fetchUserDetails2() {
        viewModelScope.launch {
            try {
                val response = repository.getUserDetails2()
                _userDetails2.value = response
            } catch (e: Exception) {
                _userDetails2.value = null
            }
        }
    }
}
