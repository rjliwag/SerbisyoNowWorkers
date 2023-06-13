package com.example.serbisyonowv1

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var database : FirebaseDatabase
    private val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

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
        val sexArray = arrayOf("Sex", "Male", "Female")

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
                    val updatedSexArray = sexArray.filter { it != "Sex" }.toTypedArray()
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


        //database register

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val fname1 : TextInputEditText = findViewById(R.id.fname)
        val lname1 : TextInputEditText = findViewById(R.id.lname)
        val email1 : TextInputEditText = findViewById(R.id.email_reg)
        val pass1 : TextInputEditText = findViewById(R.id.password_reg)
        val passlayout1: TextInputLayout = findViewById(R.id.passlayout)
        val phone1 : TextInputEditText = findViewById(R.id.phone)
        val address1 : TextInputEditText = findViewById(R.id.address)
        val birthdate1 : EditText = findViewById(R.id.birthdate)
        val sex1 : Spinner = findViewById(R.id.spinnerSex)
        val registerbtn: Button = findViewById(R.id.registerbtn)
        val registerprogbar: ProgressBar = findViewById(R.id.registerProgbar)

        val signintext : TextView = findViewById(R.id.text_create_reg)
        signintext.setOnClickListener {
            val intent = Intent(this, LoginActivity ::class.java)
            startActivity(intent)
        }

        registerbtn.setOnClickListener{
            val fname = fname1.text.toString()
            val lname = lname1.toString()
            val email = email1.text.toString()
            val pass = pass1.text.toString()

            val phone = phone1.text.toString()
            val address = address1.text.toString()
            val birthdate = birthdate1.text.toString()
            val sex = sex1.selectedItem.toString()

            registerprogbar.visibility = View.VISIBLE
            passlayout1.isPasswordVisibilityToggleEnabled = true


            if(fname.isEmpty() || lname.isEmpty() || email.isEmpty() ||pass.isEmpty() ||phone.isEmpty() ||address.isEmpty() ||birthdate.isEmpty() ||sex.isEmpty() ){
                if(fname.isEmpty()){
                    fname1.error = "Enter your Firstname"
                }
                if(lname.isEmpty()){
                    lname1.error = "Enter your Lastname"
                }
                if(email.isEmpty()){
                    email1.error = "Enter your Email"
                }
                if(pass.isEmpty()){
                    passlayout1.isPasswordVisibilityToggleEnabled = false
                    pass1.error = "Enter your Password"
                }
                if(phone.isEmpty()){
                    phone1.error = "Enter your Phone no."
                }
                if(address.isEmpty()){
                    address1.error = "Enter your Address"
                }

                if(birthdate.isEmpty()){
                    birthdate1.error = "Enter your Birthday"
                }
               Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
                registerprogbar.visibility = View.GONE
            }else if(!email.matches(emailpattern.toRegex())){
                registerprogbar.visibility = View.GONE
                email1.error = "Enter valid Email Address"
                Toast.makeText(this, "Enter Valid Email Address", Toast.LENGTH_SHORT).show()

            }else if(phone.length != 10){
                registerprogbar.visibility = View.GONE
                phone1.error = "Enter a Valid Phone Number"
                Toast.makeText(this, "Enter Valid Phone Number" , Toast.LENGTH_SHORT).show()
            }else if(pass.length <= 8){
                registerprogbar.visibility = View.GONE
                pass1.error = "Enter a Password more than 8 character"
                Toast.makeText(this, "Enter a Password atleast 8 character" , Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val databaseRef = database.reference.child("users").child(firebaseAuth.currentUser!!.uid)
                        val user = Users(
                            firstname = fname,
                            lastname = lname,
                            emailadd = email,
                            phoneno = phone,
                            addressnow = address,
                            birthdate2 = birthdate,
                            sex3 = sex,
                            uid = firebaseAuth.currentUser!!.uid
                        )

                        databaseRef.setValue(user).addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        }

    }

