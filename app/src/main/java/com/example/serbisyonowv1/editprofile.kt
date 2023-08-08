package com.example.serbisyonowv1



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


import com.example.serbisyonowv1.databinding.ActivityEditprofileBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class editprofile : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.savebtn.setOnClickListener {

            checkUser()


        }


        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        retrieveAndPopulateUserData()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        } else {
            val uid = firebaseUser.uid // Get the uid of the current user
            updateData(uid)

        }

    }

    private fun updateData(uid: String) {
        val fname = binding.fstname.text.toString()
        val lname = binding.lstname.text.toString()
        val phone = binding.phoneno.text.toString()
        val address = binding.useraddress.text.toString()


        database = FirebaseDatabase.getInstance().getReference("Users")
        val user = mapOf<String, String>(
            "firstname" to fname,
            "lastname" to lname,
            "phoneno" to phone,
            "address" to address
        )
        database.child(uid).updateChildren(user).addOnSuccessListener {
            startActivity(Intent(this, requestorsProfile::class.java))
            finish()
            Toast.makeText(this, "Success to Update", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
        }


    }

    private fun retrieveAndPopulateUserData() {
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            val uid = firebaseUser.uid
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(uid).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val firstname = dataSnapshot.child("firstname").value
                    val lastname = dataSnapshot.child("lastname").value
                    val phoneno = dataSnapshot.child("phoneno").value
                    val address = dataSnapshot.child("address").value

                    // Set retrieved data in EditText fields
                    binding.fstname.setText(firstname.toString())
                    binding.lstname.setText(lastname.toString())
                    binding.phoneno.setText(phoneno.toString())
                    binding.useraddress.setText(address.toString())
                }
            }
        }

    }
}
