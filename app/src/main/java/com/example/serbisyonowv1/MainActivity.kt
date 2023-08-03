package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lets get started button function
        val textCreate = findViewById<TextView>(R.id.startbutton)
        textCreate.setOnClickListener {
            // Handle the click event here

            val intent = Intent(this, Login::class.java)
            startActivity(intent)


        }
    }
}