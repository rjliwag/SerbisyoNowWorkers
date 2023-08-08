package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.serbisyonowv1.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class forgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mAuth = FirebaseAuth.getInstance()

        binding.resetbtn.setOnClickListener{
           val  email = binding.emailAdd.text.trim()
            if (!TextUtils.isEmpty(email)){
                ResetPassword(email)

            }else{
                binding.emailAdd.setError("Email is empty!")

            }
        }


        binding.backbtn.setOnClickListener{
            onBackPressed()
        }


    }
    private fun ResetPassword(email: CharSequence){
        binding.backbtn.isVisible = false

        mAuth.sendPasswordResetEmail(email.toString()).addOnSuccessListener {
            Toast.makeText(this, "Reset Password link has been sent to your registration Email", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Login::class.java))
            finish()
        }.addOnFailureListener{e ->
            Toast.makeText(this, "Error : -" + e.message, Toast.LENGTH_SHORT).show()
            binding.resetbtn.isVisible = true
        }

    }
}


