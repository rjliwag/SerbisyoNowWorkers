package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.serbisyonowv1.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class dashboard : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()





        replaceFragment(Searchpage())



        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_search -> replaceFragment(Searchpage())
                R.id.menu_jobs -> replaceFragment(jobpage())
                R.id.menu_messages -> replaceFragment(messagepage())
                R.id.menu_notification -> replaceFragment(notifpage())
                R.id.menu_profile -> replaceFragment(prifeFragment())
                else -> {
                }
            }
            true
        }

    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, splashactivity::class.java))
            finish()
        }
        else{







        }
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }


}