package com.example.sendnotification.Modal

data class SendData(
    val token: String,
    val title: String,
    val message: String
)

data class UserDetail(
    val id: Int,
    val name: String,
    val deviceToken: String
)

data class ApiResponse(
    val host: UserDetail,
    val hostSubscribers: List<UserDetail>
)


data class Room(
    val id: String = "",
    val title: String = "",
    val dateTime: String = "",
    val hostId: String = "",
    val participantIds: MutableList<String> = mutableListOf()
)


data class Creator(
    val name: String,
    val profession: String,
    val imageUrl: String
)


