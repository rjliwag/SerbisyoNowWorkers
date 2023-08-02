package com.example.serbisyonowv1

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.serbisyonowv1.databinding.ActivityRegisterBinding

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar




class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var phoneNoEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var birthdate: EditText
    private lateinit var  spinnersex : Spinner
    private lateinit var regButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



//----------------------------------------------------------------------------------------

        //bithday calendar
        val birthday = binding.birthdate
        val cal = Calendar.getInstance()
        val Myear = cal.get(Calendar.YEAR)
        val Mmonth = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)



        // birthday calendar show
        birthday.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val dateString = "$dayOfMonth / ${month + 1} / $year"
                    birthday.setText(dateString)
                },
                Myear,
                Mmonth,
                day
            )
            datePickerDialog.show()
        }

        val spinnerSex = findViewById<Spinner>(R.id.spinnerSex)
        val sexArray = arrayOf("Gender","Male", "Female","Others")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSex.adapter = adapter

        // Set a listener to handle item selection
        spinnerSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Exclude the first position (hint item)
                if (position > 0) {
                    val selectedSex = parent?.getItemAtPosition(position).toString()

                    // Remove the "Sex" option from the dropdown
                    val updatedSexArray = sexArray.filter { it != "Gender" }.toTypedArray()
                    val updatedAdapter = ArrayAdapter(this@RegisterActivity, android.R.layout.simple_spinner_item, updatedSexArray)
                    updatedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerSex.adapter = updatedAdapter

                    // Find the position of the selected sex in the updated adapter
                    val selectedPosition = updatedAdapter.getPosition(selectedSex)

                    // Set the correct selection position
                    spinnerSex.setSelection(selectedPosition)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }



        }
    private fun register(){

        regButton.setOnClickListener{

            if(TextUtils.isEmpty(firstNameEditText.text.toString())){
                firstNameEditText.setError("Please enter first name")

            }else if (TextUtils.isEmpty(lastNameEditText.text.toString())){
                lastNameEditText.setError("Please Enter Last Name")
            }else if (TextUtils.isEmpty(emailEditText.text.toString())){
                emailEditText.setError("Please Enter Last Name")
            }else if (TextUtils.isEmpty(passwordEditText.text.toString())){
                passwordEditText.setError("Please Enter Last Name")
            }else if (TextUtils.isEmpty(phoneNoEditText.text.toString())){
                phoneNoEditText.setError("Please Enter Last Name")
            }else if (TextUtils.isEmpty(addressEditText.text.toString())){
                addressEditText.setError("Please Enter Last Name")
            }else if (TextUtils.isEmpty(birthdate.text.toString())){
                birthdate.setError("Please Enter Last Name")
            }

          /*  firebaseAuth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val currentUser = firebaseAuth.currentUser
                        val currentUserDb = reference?.child(currentUser?.uid!!)
                        currentUserDb?.child("firstname")?.setValue(firstNameEditText.text.toString())
                        startActivity(Intent(this, MainPageActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Registration Failed, Please try Again!", Toast.LENGTH_LONG).show()
                    }
                }*/


        }
    }

    }

