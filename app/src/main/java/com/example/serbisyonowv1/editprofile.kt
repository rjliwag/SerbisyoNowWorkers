package com.example.serbisyonowv1



import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.serbisyonowv1.databinding.ActivityEditprofileBinding
import com.google.android.gms.tasks.Task
import com.google.android.play.integrity.internal.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


@Suppress("IMPLICIT_CAST_TO_ANY")
class editprofile : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private var imageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)


        binding.addprofile.setOnClickListener {

            showImageSourceDialog()
        }

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
                0 -> pickImageCamera()
                1 -> pickImageGallery()
            }
        }
        builder.show()
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                //imageUri = data!!.data

                binding.profileimg.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                binding.profileimg.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )


    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        } else {
            val uid = firebaseUser.uid // Get the uid of the current user

            if (imageUri == null) {
                updateData(uid, "") // Pass an empty string here
            } else {
                uploadImage(uid)
            }
        }
    }



    private fun uploadImage(uid: String) {
        progressDialog.setMessage("Uploading Profile Image")
        progressDialog.show()

        val filePathAndName = "ProfileImages/$uid.jpg"

        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val uploadImageUrl = uri.toString()
                    updateData(uid, uploadImageUrl) // Pass the uploadImageUrl here
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Upload due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }



    private fun updateProfile(uploadImageUrl: String) {
        progressDialog.setMessage("Updating Profile...")

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["ImageProfile"] = uploadImageUrl

        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, requestorsProfile::class.java))
                finish()
                Toast.makeText(this, "Success to Update", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Failed to update Profile due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun updateData(uid: String, uploadImageUrl: String) {
        progressDialog.setMessage("Updating Profile...")

        val fname = binding.fstname.text.toString()
        val lname = binding.lstname.text.toString()
        val phone = binding.phoneno.text.toString()
        val address = binding.useraddress.text.toString()

        val user = mutableMapOf<String, Any>(
            "firstname" to fname,
            "lastname" to lname,
            "phoneno" to phone,
            "address" to address
        )

        if (uploadImageUrl.isNotEmpty()) {
            user["ImageProfile"] = uploadImageUrl
        }

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uid).updateChildren(user).addOnSuccessListener {
            progressDialog.dismiss()
            startActivity(Intent(this, requestorsProfile::class.java))
            finish()
            Toast.makeText(this, "Success to Update", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            progressDialog.dismiss()
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
                    val profileImageUrl = dataSnapshot.child("ImageProfile").value // Retrieve profile image URL

                    // Set retrieved data in EditText fields
                    binding.fstname.setText(firstname.toString())
                    binding.lstname.setText(lastname.toString())
                    binding.phoneno.setText(phoneno.toString())
                    binding.useraddress.setText(address.toString())

                    // Load profile image using Picasso if the profileImageUrl is not null
                    if (profileImageUrl != null && profileImageUrl.toString().isNotEmpty()) {
                        // Load profile image using Picasso
                        Picasso.get().load(profileImageUrl.toString()).into(binding.profileimg)
                    }
                }
            }
        }
    }

}



