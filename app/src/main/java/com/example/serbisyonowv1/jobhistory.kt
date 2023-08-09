package com.example.serbisyonowv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.serbisyonowv1.databinding.ActivityJobhistoryBinding


class jobhistory : AppCompatActivity() {
    private lateinit var binding: ActivityJobhistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobhistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

    }
}