package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
   private lateinit var emailadd : TextInputEditText
  private  lateinit var passwordlogin : TextInputEditText
   private lateinit var loginbutton: Button







    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loginbutton = findViewById(R.id.loginbutton)
        emailadd = findViewById(R.id.email_add)
        passwordlogin = findViewById(R.id.password_login)

        login()
    }

    private fun login() {
        if (loginbutton == null) {
            return
        }
            loginbutton.setOnClickListener {
                if (TextUtils.isEmpty(emailadd.text.toString())) {
                    emailadd.setError("Please Enter Email Address")
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(passwordlogin.text.toString())) {
                    passwordlogin.setError("Please Enter Password")
                    return@setOnClickListener
                }
                auth.signInWithEmailAndPassword(
                    emailadd.text.toString(),
                    passwordlogin.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainPageActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Login Failed, Please Try Again!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }


}