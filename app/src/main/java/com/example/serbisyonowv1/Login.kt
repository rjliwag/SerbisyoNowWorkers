package com.example.serbisyonowv1


import android.app.ProgressDialog
import android.content.Intent
import android.net.ConnectivityManager
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
        binding.forgotText.setOnClickListener {
            startActivity(Intent(this, forgotPassword::class.java))
            finish()
        }
    }

    private var email = ""
    private var password =""

    private fun validateData(){
        email = binding.emailAdd.text.toString().trim()
        password = binding.passwordLogin.text.toString().trim()

        if(email.isEmpty()) {
            binding.emailAdd.setError("Please Input your Email")

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailAdd.setError("Invalid Email")
        }
        else if (password.isEmpty()) {
            binding.passwordLogin.setError("Please Enter Password")

        } else{
            loginUser()
        }
    }

    private fun loginUser() {
        if (!isNetworkAvailable()) {
            progressDialog.dismiss()
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_LONG).show()
            return
        }

        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Incorrect username or password. Please try again.", Toast.LENGTH_LONG).show()
            }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    private fun checkUser() {
        progressDialog.setMessage("Checking User....")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    startActivity(Intent(this@Login , dashboard::class.java))
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
}


