package com.example.serbisyonowv1

import UserDataViewModel
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.serbisyonowv1.databinding.ActivityDashboardBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class dashboard : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityDashboardBinding
    lateinit var userDataViewModel: UserDataViewModel
    private lateinit var username: TextView
    private lateinit var emailview: TextView
    private lateinit var lname: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var toggle : ActionBarDrawerToggle
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.tool_bar_dash)



        setSupportActionBar(toolbar)

        // Hide default title


        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_profile -> startActivity(Intent(this, requestorsProfile::class.java))
                R.id.nav_history -> startActivity(Intent(this, jobhistory::class.java))
                R.id.nav_settings -> startActivity(Intent(this, settings::class.java))
                R.id.nav_logout -> logoutUser()

            }
            true
        }



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_profile)





        replaceFragment(Searchpage())



        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_search -> {
                    binding.toolbartitle.text = "Workers"

                    replaceFragment(Searchpage())
                }
                R.id.menu_jobs -> {
                    binding.toolbartitle.text = "Dashboard"
                    replaceFragment(jobpage())
                }
                R.id.menu_messages -> {
                    binding.toolbartitle.text = "Messages"
                    replaceFragment(messagepage())
                }
                R.id.menu_notification -> {
                    binding.toolbartitle.text = "Notification"
                    replaceFragment(notifpage())
                }

                else -> {
                }
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        else{
            val uid = firebaseUser.uid // Get the uid of the current user
            readData(uid)

        }

    }

    private fun readData(uid: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uid).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val uid = it.child("uid").value
                    val fullname = it.child("firstname").value
                    val lastname = it.child("lastname").value
                    val email = it.child("email").value

                    // Set the data in the ViewModel
                    userDataViewModel.uid = uid as? String
                    userDataViewModel.fullname = fullname as? String
                    userDataViewModel.email = email as? String

                    val fname = userDataViewModel.fullname
                    val cemail = userDataViewModel.email



                    val navigationView: NavigationView = findViewById(R.id.nav_view)

                    // Get the header view (your nav_header_layout.xml)
                    val headerView = navigationView.getHeaderView(0)
                    username = headerView.findViewById(R.id.username)

                    emailview = headerView.findViewById(R.id.currentemail)
                    lname = headerView.findViewById(R.id.lname)

                    username.text = fname
                    emailview.text = cemail
                    lname.text = lastname.toString()





                }
                else{
                    Toast.makeText(this, "User Not existed", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

    }



    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    private fun logoutUser() {
        // Perform logout logic (in this case, using Firebase Auth)
        firebaseAuth.signOut()

        // Navigate back to the login screen
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Optional: You can finish the current activity to prevent the user from returning to it using the back button.
    }


}