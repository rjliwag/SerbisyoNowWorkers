package com.example.serbisyonowv1


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.serbisyonowv1.databinding.ActivityLogin2Binding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLogin2Binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.textCreateLogin.setOnClickListener{
            startActivity(Intent(this, registeract::class.java))
        }
        binding.loginbutton.setOnClickListener {
            validateData()
        }

    }

    private var email = ""
    private var pass =""

    private fun validateData(){
        email = binding.emailAdd.text.toString().trim()
        pass = binding.passwordLogin.text.toString().trim()

        if(email.isEmpty()) {
            binding.emailAdd.setError("Please Input your Email")

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailAdd.setError("Invalid Email")
        }
        else if (pass.isEmpty()) {
            binding.passwordLogin.setError("Please Enter Password")

        } else{
            loginUser()
        }
    }

    private fun loginUser() {

        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                checkUser()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Login Failed due to $e.message", Toast.LENGTH_LONG).show()
            }

    }

    private fun checkUser() {
        progressDialog.setMessage("Checking User....")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(firebaseUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.dismiss()

                // Check if the snapshot exists and contains the user's data
                if (snapshot.exists()) {
                    // Get the user data from the snapshot
                    val userData = snapshot.getValue(User::class.java)

                    // Pass the user data to the dashboard activity
                    val dashboardIntent = Intent(this@Login, dashboard::class.java)
                    dashboardIntent.putExtra("userData", userData)
                    startActivity(dashboardIntent)
                    finish()
                } else {
                    // If the user's data does not exist in the database, show an error message
                    Toast.makeText(this@Login, "User does not exist. Please register first.", Toast.LENGTH_LONG).show()
                    // Optionally, you can redirect the user to the registration page here
                    // startActivity(Intent(this@Login, registeract::class.java))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(this@Login, "Database Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}


