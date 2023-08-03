package com.example.serbisyonowv1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serbisyonowv1.databinding.ActivityLesdodisBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class lesdodis : AppCompatActivity() {

    private lateinit var binding: ActivityLesdodisBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLesdodisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutbtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        else{
            val uid = firebaseUser.uid // Get the uid of the current user
            readData(uid)

        }

    }

    private fun readData(uid: String){
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uid).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val fullname = it.child("fullname").value
                    val email = it.child("email").value
                    val address = it.child("address").value
                    Toast.makeText(this, "Successfully Retrieve",Toast.LENGTH_LONG).show()

                    binding.fullname.text = fullname.toString()
                    binding.email.text = email.toString()
                    binding.address.text = address.toString()

                }
                else{
                    Toast.makeText(this, "User Not existed", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

    }
}