package com.example.sendnotification.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sendnotification.Adapter.UserAdapter
import com.example.sendnotification.Modal.User
import com.example.sendnotification.R
import com.example.sendnotification.ViewModel.UserViewModel
import com.example.sendnotification.databinding.ActivityDashboardBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    private lateinit var views: ActivityDashboardBinding
    private val sharedPrefFile = "kotlinsharedpreference"
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private val users = mutableListOf<User>()
    private lateinit var currentUserEmail: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(views.root)


        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        currentUserEmail = sharedPreferences.getString("email", "") ?: ""


        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(users) { user ->
            onUserItemClick(user)
            Log.d("bbb","data = ${user}")
        }
        recyclerView.adapter = userAdapter



        userViewModel.users.observe(this) { userList ->
            if (userList != null) {
                val filteredUsers = userList.filter { it.email != currentUserEmail }
                users.clear()
                users.addAll(filteredUsers)
                userAdapter.notifyDataSetChanged()
            }
        }

        userViewModel.isLoading.observe(this) { isLoading ->
            findViewById<ProgressBar>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        userViewModel.fetchUsers()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        views.logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }


    }

    private fun onUserItemClick(user: User) {
        Toast.makeText(this, "Clicked: ${user.name}", Toast.LENGTH_SHORT).show()

    }
}