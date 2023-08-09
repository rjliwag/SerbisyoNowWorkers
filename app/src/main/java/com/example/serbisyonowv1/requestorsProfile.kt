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
import com.squareup.picasso.Picasso

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

        binding.edittext.setOnClickListener {
            startActivity(Intent(this, editprofile::class.java))
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
                    val fullname = it.child("firstname").value
                    val lstname = it.child("lastname").value
                    val address = it.child("address").value
                    val emailadd = it.child("email").value
                    val birthday = it.child("birthday").value
                    val gender = it.child("gender").value
                    val profilepic = it.child("ImageProfile").value


                    binding.fullname.text = "$fullname"
                    binding.lname.text = "$lstname"
                    binding.userid.text= "$uid"
                    binding.useraddress.text = "$address"
                    binding.emailAdd.text = "$emailadd"
                    binding.userbday.text = "$birthday"
                    binding.usergender.text = "$gender"

                    val profileImage = binding.profileimg // Replace with your ImageView ID in the layout
                    if (profilepic != null && profilepic.toString().isNotEmpty()) {
                        Picasso.get().load(profilepic.toString()).into(profileImage)
                    }

                }
                else{
                    Toast.makeText(this, "User Not existed", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

    }
}