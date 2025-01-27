package com.example.sendnotification.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sendnotification.R


import android.content.Intent
import android.widget.Button

class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val hostButton: Button = findViewById(R.id.hostButton)
        val subscriberButton: Button = findViewById(R.id.subscriberButton)

        hostButton.setOnClickListener {
            startActivity(Intent(this, HostActivity::class.java))
        }

        subscriberButton.setOnClickListener {
            startActivity(Intent(this, SubscriberActivity::class.java))
        }
        val notificationButton: Button = findViewById(R.id.notificationButton)
        notificationButton.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }
}
