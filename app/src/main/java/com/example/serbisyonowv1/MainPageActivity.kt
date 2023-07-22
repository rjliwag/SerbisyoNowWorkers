package com.example.serbisyonowv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.serbisyonowv1.databinding.ActivityMainPageBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainPageBinding
    private lateinit var auth :FirebaseAuth

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)


        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null){
            val intent = Intent(this, LoginActivity ::class.java)
            startActivity(intent)
        }


        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

}