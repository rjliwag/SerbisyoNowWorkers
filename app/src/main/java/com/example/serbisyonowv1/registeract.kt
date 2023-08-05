package com.example.serbisyonowv1

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.example.serbisyonowv1.databinding.ActivityMainBinding
import com.example.serbisyonowv1.databinding.ActivityRegisteractBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.regex.Pattern


class registeract : AppCompatActivity() {


    private lateinit var registeractBinding: ActivityRegisteractBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registeractBinding = ActivityRegisteractBinding.inflate(layoutInflater)
        setContentView(registeractBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        val backbtn : ImageButton = findViewById(R.id.backbtn)

        backbtn.setOnClickListener{
            onBackPressed()
        }

        registeractBinding.registerbtn.setOnClickListener{
            validateData()
        }

        val birthday = registeractBinding.birthdate
        val cal = Calendar.getInstance()
        val Myear = cal.get(Calendar.YEAR)
        val Mmonth = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

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
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Exclude the first position (hint item)
                if (position > 0) {
                    val selectedSex = parent?.getItemAtPosition(position).toString()

                    // Remove the "Sex" option from the dropdown
                    val updatedSexArray = sexArray.filter { it != "Gender" }.toTypedArray()
                    val updatedAdapter = ArrayAdapter(
                        this@registeract,
                        android.R.layout.simple_spinner_item,
                        updatedSexArray
                    )
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

    private var fname =""
    private var lname=""
    private var email = ""
    private var pass = ""
    private var phoneno = ""
    private var address = ""
    private var birtdate = ""
    private var gender = ""




    private fun validateData(){
        fname = registeractBinding.fname.text.toString().trim()
        lname = registeractBinding.lname.text.toString().trim()
        email = registeractBinding.emailReg.text.toString().trim()
        pass = registeractBinding.passwordReg.text.toString().trim()
        phoneno = registeractBinding.phone.text.toString().trim()
        address = registeractBinding.address.text.toString().trim()
        birtdate = registeractBinding.birthdate.text.toString().trim()
        gender = registeractBinding.spinnerSex.selectedItem.toString().trim()

        if (fname.isEmpty()) {
            registeractBinding.fname.setError("Please Enter First Name")
        } else if (lname.isEmpty()) {
            registeractBinding.lname.setError("Please Enter Last Name")
        } else if (email.isEmpty()) {
            registeractBinding.emailReg.setError("Please Enter Email Address")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registeractBinding.emailReg.setError("Invalid Email Address")
        } else if (pass.isEmpty()) {
            registeractBinding.passwordReg.setError("Please Enter Password")
        } else if (phoneno.isEmpty()) {
            registeractBinding.phone.setError("Please Enter Phone Number")
        } else if (address.isEmpty()) {
            registeractBinding.address.setError("Please Enter Address")
        } else {
            createUserAccount()
        }

    }
    private fun createUserAccount(){
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{
                updateUserInfo()
            }
            .addOnCompleteListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this@registeract, "Failed Creating Account due to $e.message", Toast.LENGTH_LONG).show()
            }
    }
    private fun updateUserInfo(){
        progressDialog.setMessage("Saving user Info...")

        val timestamp = System.currentTimeMillis()

      val uid = firebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["fullname"] = fname +" "+ lname
        hashMap["email"] = email
        hashMap["phoneno"] = phoneno
        hashMap["address"] = address
        hashMap["birthday"] = birtdate
        hashMap["gender"] = gender
        hashMap["ImageProfile"] = ""
        hashMap["userType"]="requestor"
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Account Created!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, Login::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed Saving User's Info due to ${e.message}", Toast.LENGTH_LONG).show()
            }



    }
}