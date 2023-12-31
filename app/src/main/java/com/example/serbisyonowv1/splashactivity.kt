package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class splashactivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashactivity)

        firebaseAuth = FirebaseAuth.getInstance()
        lifecycleScope.launch {
            delay(2000)
            checkuser()
        }
    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, dashboard::class.java))
        } else {
            val ref = FirebaseDatabase.getInstance().getReference("Users")

            ref.child(firebaseUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        startActivity(Intent(this@splashactivity, Login::class.java))
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
    }


