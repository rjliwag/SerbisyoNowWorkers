package com.example.serbisyonowv1

import UserDataViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.serbisyonowv1.databinding.ActivityLogin2Binding
import com.example.serbisyonowv1.databinding.ActivityRequestorsProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class requestorsProfile : AppCompatActivity() {
    private lateinit var binding: ActivityRequestorsProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestorsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.backpress.setOnClickListener{
            onBackPressed()
            finish()
        }




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

    private fun readData(uid: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uid).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val uid = it.child("uid").value
                    val fullname = it.child("fullname").value
                    val address = it.child("address").value
                    val emailadd = it.child("email").value


                    binding.fullname.text = "$fullname"
                    binding.userid.text= "$uid"
                    binding.useraddress.text = "$address"
                    binding.emailAdd.text = "$emailadd"

                }
                else{
                    Toast.makeText(this, "User Not existed", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

    }
}