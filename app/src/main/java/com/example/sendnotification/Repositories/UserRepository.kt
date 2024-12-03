package com.example.sendnotification.Repositories

import com.example.sendnotification.Modal.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class UserRepository {
    private val db = Firebase.firestore.collection("users")

    fun addUser(user: User, onComplete: (Boolean) -> Unit) {
        db.document(user.token).set(user)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getUsers(onComplete: (List<User>?) -> Unit) {
        db.get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
                onComplete(users)
            }
            .addOnFailureListener { onComplete(null) }
    }
}
