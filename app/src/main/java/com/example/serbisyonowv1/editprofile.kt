package com.example.serbisyonowv1



import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.serbisyonowv1.databinding.ActivityEditprofileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class editprofile : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private val cameraPermission = Manifest.permission.CAMERA
    private val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE





    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {

        binding.profileimg.setImageBitmap(it)


    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {

        binding.profileimg.setImageURI(it)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addprofile.setOnClickListener{

            showImageSourceDialog()
        }


        firebaseAuth = FirebaseAuth.getInstance()


        binding.savebtn.setOnClickListener {

            checkUser()


        }


        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        retrieveAndPopulateUserData()
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermissionAndOpenCamera()
                1 -> checkStoragePermissionAndOpenGallery()
            }
        }
        builder.show()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(cameraPermission), REQUEST_CAMERA)
        }
    }

    private fun checkStoragePermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, storagePermission) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(storagePermission), REQUEST_GALLERY)
        }
    }

    private fun openCamera() {
        cameraLauncher.launch(null)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_CAMERA -> openCamera()
                REQUEST_GALLERY -> openGallery()
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_GALLERY = 2
    }


    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        } else {
            val uid = firebaseUser.uid // Get the uid of the current user
            updateData(uid)

        }

    }





    private fun updateData(uid: String) {
        val fname = binding.fstname.text.toString()
        val lname = binding.lstname.text.toString()
        val phone = binding.phoneno.text.toString()
        val address = binding.useraddress.text.toString()





        database = FirebaseDatabase.getInstance().getReference("Users")
        val user = mapOf<String, Any>(
            "firstname" to fname,
            "lastname" to lname,
            "phoneno" to phone,
            "address" to address
        )
        database.child(uid).updateChildren(user).addOnSuccessListener {
            startActivity(Intent(this, requestorsProfile::class.java))
            finish()
            Toast.makeText(this, "Success to Update", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
        }


    }

    private fun retrieveAndPopulateUserData() {
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            val uid = firebaseUser.uid
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(uid).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val firstname = dataSnapshot.child("firstname").value
                    val lastname = dataSnapshot.child("lastname").value
                    val phoneno = dataSnapshot.child("phoneno").value
                    val address = dataSnapshot.child("address").value

                    // Set retrieved data in EditText fields
                    binding.fstname.setText(firstname.toString())
                    binding.lstname.setText(lastname.toString())
                    binding.phoneno.setText(phoneno.toString())
                    binding.useraddress.setText(address.toString())
                }
            }
        }

    }
}
