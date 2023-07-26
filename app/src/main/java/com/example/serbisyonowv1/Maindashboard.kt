package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.serbisyonowv1.databinding.ActivityMainBinding
import com.example.serbisyonowv1.databinding.ActivityMaindashboardBinding
import com.example.serbisyonowv1.databinding.HeaderBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Maindashboard : AppCompatActivity() {

    private lateinit var binding: ActivityMaindashboardBinding
    private lateinit var headerBinding: HeaderBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaindashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)




        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()





    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, Login::class.java))
            finish()
        }else{

            val email = firebaseUser.email
            val databaseRef = FirebaseDatabase.getInstance().getReference("Users")


            // Query to find the user with the matching email
            val query = databaseRef.orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            // Get the user's information from the database
                            val firstName = userSnapshot.child("firstname").getValue(String::class.java)
                            val lastName = userSnapshot.child("lastname").getValue(String::class.java)

                            // Update the TextViews in the navigation header
                            val navigationView = findViewById<NavigationView>(R.id.nav_view)
                            val headerView = navigationView.getHeaderView(0)
                            val currentNameTextView = headerView.findViewById<TextView>(R.id.currentname)
                            val currentLastNameTextView = headerView.findViewById<TextView>(R.id.currentlastname)
                            val currentEmailTextView = headerView.findViewById<TextView>(R.id.currentemail)

                            currentEmailTextView.text = email
                            currentNameTextView.text = firstName
                            currentLastNameTextView.text = lastName
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors that occur during the database operation
                }
            })
        }
    }
}