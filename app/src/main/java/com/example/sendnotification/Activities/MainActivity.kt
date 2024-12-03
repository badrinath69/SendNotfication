package com.example.sendnotification.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sendnotification.R
import com.example.sendnotification.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var views: ActivityMainBinding
    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)

        FirebaseApp.initializeApp(this)
        firebaseFirestore = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        views.googlesignbtn.setOnClickListener { view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "ooo"+"${e.toString()}", Toast.LENGTH_SHORT).show()
            Log.d("bbb",e.toString())
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val name = account.displayName ?: "Unknown"
                val email = account.email ?: "Unknown"
                val userId = firebaseAuth.currentUser?.uid ?: ""
                updateFCMTokenOnLogin(userId)


                FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                    if (tokenTask.isSuccessful) {
                        val token = tokenTask.result ?: "UnknownToken"

                        val user = mapOf(
                            "name" to name,
                            "email" to email,
                            "token" to token
                        )

                        FirebaseFirestore.getInstance().collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                                    sharedPrefFile, Context.MODE_PRIVATE
                                )
                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("email", email)
                                editor.putString("name", name)
                                editor.apply()

                                val intent = Intent(this, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Failed to save user data: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to get FCM token",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
            }
        }

    fun updateFCMTokenOnLogin(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val newToken = task.result
                if (newToken != null) {
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("fcmToken")
                    userRef.setValue(newToken).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d("FCM", "FCM token updated successfully for user: $userId")
                        } else {
                            Log.e("FCM", "Error updating FCM token: ${updateTask.exception}")
                        }
                    }
                } else {
                    Log.e("FCM", "Failed to fetch FCM token")
                }
            } else {
                Log.e("FCM", "Error retrieving FCM token: ${task.exception}")
            }
        }
    }



    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, DashboardActivity
                    ::class.java
                )
            )
            finish()
        }
    }
}