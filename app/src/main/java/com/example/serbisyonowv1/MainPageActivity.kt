package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.serbisyonowv1.databinding.ActivityMainPageBinding
import com.google.android.material.navigation.NavigationBarMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainPageBinding
    private lateinit var auth :FirebaseAuth
     var reference: DatabaseReference? = null
     var database: FirebaseDatabase? = null
    private lateinit var navigationView: NavigationView // Declare navigationView here
    private lateinit var currentname: TextInputEditText
    private lateinit var currentemail: TextInputEditText
    private lateinit var currentlastname: TextInputEditText



    val navigationViewmenu: NavigationView = findViewById(R.id.nav_view)

    private fun logout() {
        navigationViewmenu.setNavigationItemSelectedListener {

                menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {

                    auth.signOut()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }





    private lateinit var drawerLayout : DrawerLayout

    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize navigationView here after calling setContentView
        navigationView = findViewById(R.id.nav_view)

        // Move these lines here after navigationView is initialized
        val headerView: View = navigationView.getHeaderView(0)
        currentname = headerView.findViewById(R.id.fullname)
        currentemail = headerView.findViewById(R.id.currentemail)




        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database?.reference!!.child("profile")



        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null){
            val intent = Intent(this, Login ::class.java)
            startActivity(intent)
        }



        replaceFragment(Searchpage())

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)


        toolbar = findViewById(R.id.toolbar)





        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open, // Set your own string resource for "open drawer" accessibility description
            R.string.close // Set your own string resource for "close drawer" accessibility description
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {R.drawable.ic_profile}



        binding.bottomNavigation.setOnItemSelectedListener{
            when (it.itemId){
                R.id.menu_search -> replaceFragment(Searchpage())
                R.id.menu_jobs -> replaceFragment(jobpage())
                R.id.menu_messages -> replaceFragment(messagepage())
                R.id.menu_notification -> replaceFragment(notifpage())

                else->{

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    private fun loadProfile(){

        val user = auth.currentUser
        val userreference = reference?.child(user?.uid!!)

        currentemail.text = Editable.Factory.getInstance().newEditable("Email - ->"+user?.email)

        userreference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                currentname.text = Editable.Factory.getInstance().newEditable("Firstname - - >"+snapshot.child("firstname").value.toString())
                currentlastname.text = Editable.Factory.getInstance().newEditable("Lastname - - >"+snapshot.child("latname").value.toString())





            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }

}