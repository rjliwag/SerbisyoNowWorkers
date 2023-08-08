package com.example.serbisyonowv1


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.serbisyonowv1.databinding.ActivityEditprofileBinding



class editprofile : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.icBack.setOnClickListener {
            onBackPressed()
        }



    }



}