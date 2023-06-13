package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.serbisyonowv1.databinding.ActivityLoginBinding
import com.example.serbisyonowv1.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth

    private val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

            val emailadd: TextInputEditText = findViewById(R.id.email_add)
            val password: TextInputEditText = findViewById(R.id.password_login)
            val passlayout1: TextInputLayout = findViewById(R.id.passlayout1)
            val loginbtn: Button = findViewById(R.id.loginbutton)
            val loginprogbar: ProgressBar = findViewById(R.id.signinProgbar)

       val  registertext: TextView = findViewById(R.id.text_create_login)

        registertext.setOnClickListener{
            val intent = Intent(this, RegisterActivity ::class.java)
            startActivity(intent)
        }

        loginbtn.setOnClickListener{
            loginprogbar.visibility = View.VISIBLE
            passlayout1.isPasswordVisibilityToggleEnabled = true

            val email = emailadd.text.toString()
            val pass = password.text.toString()

            if (email.isEmpty() || pass.isEmpty()){
                if(email.isEmpty()){
                    emailadd.error = "Enter Email Addreess"
                }
                if(pass.isEmpty()){
                    password.error = "Enter Password"
                    passlayout1.isPasswordVisibilityToggleEnabled = false
                }
                loginprogbar.visibility = View.GONE
                Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
            }else if(!email.matches(emailpattern.toRegex())){
            loginprogbar.visibility = View.GONE
            emailadd.error = "Enter valid Email Address"
            Toast.makeText(this, "Enter Valid Email Address", Toast.LENGTH_SHORT).show()

          }else if(pass.length <= 8){
            loginprogbar.visibility = View.GONE
            password.error = "Enter a Password more than 8 character"
            Toast.makeText(this, "Enter a Password atleast 8 character" , Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                if (it.isSuccessful){
                    val intent = Intent(this, MainPageActivity :: class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Email or Password are incorrect", Toast.LENGTH_SHORT).show()
                    loginprogbar.visibility = View.GONE

                }
            }
            }

        }
    }
}