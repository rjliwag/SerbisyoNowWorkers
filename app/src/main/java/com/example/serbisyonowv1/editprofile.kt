package com.example.serbisyonowv1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.serbisyonowv1.databinding.ActivityEditprofileBinding
import com.google.android.gms.cast.framework.media.ImagePicker
import java.net.URI
import android.Manifest
import android.util.Log


class editprofile : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var image = Uri.EMPTY

        val getPhoto = registerForActivityResult(ActivityResultContracts.TakePicture(),
            ActivityResultCallback {
                Log.d("Photo Taken: ", it.toString())
            })

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback { uri ->

            binding.profileimg.setImageURI(uri)
        })



        binding.addprofile.setOnClickListener {
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery")

            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Choose an option")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {
                        requestPermission()


                        if (hasCameraPermission()) {

                            getPhoto.launch(image)

                            binding.profileimg.setImageURI(image)
                        }

                    }

                    options[item] == "Choose from Gallery" -> {

                        getImage.launch("image/*")


                    }

                }
            }
            builder.show()
        }
        binding.icBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun hasCameraPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

     private fun requestPermission() {


        var permissionsToRequest = mutableListOf<String>()



        if (!hasCameraPermission()) {


            permissionsToRequest.add(Manifest.permission.CAMERA)
        }


        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }


    }
}