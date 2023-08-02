    package com.example.serbisyonowv1

    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.view.MenuItem
    import android.view.View
    import android.widget.LinearLayout
    import android.widget.TextView
    import androidx.activity.viewModels
    import androidx.appcompat.app.ActionBarDrawerToggle
    import androidx.appcompat.widget.Toolbar
    import androidx.drawerlayout.widget.DrawerLayout
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
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


        private lateinit var firebaseAuth: FirebaseAuth


        private lateinit var drawerLayout: DrawerLayout
        private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMaindashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)


            firebaseAuth = FirebaseAuth.getInstance()

           replaceFragment(Searchpage())
            checkUser()





        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {

                    startActivity(Intent(this, requestorsProfile::class.java))
                    true
                }
                R.id.nav_logout-> {

                   startActivity(Intent(this, Login::class.java))
                    true
                }
                // Add more cases for other menu items if needed
                else -> false
            }
            }


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_search -> replaceFragment(Searchpage())
                R.id.menu_jobs -> replaceFragment(jobpage())
                R.id.menu_messages -> replaceFragment(messagepage())
                R.id.menu_notification -> replaceFragment(notifpage())

                else -> {
                }
            }
            true
        }




            drawerLayout = findViewById(R.id.drawer_layout)
            actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(actionBarDrawerToggle)

            // Enable the hamburger icon for the navigation drawer
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)


        }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                true
            } else {
                // Handle other menu items here if needed
                super.onOptionsItemSelected(item)
            }
        }
        private fun replaceFragment(fragment : Fragment){

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout,fragment)
            fragmentTransaction.commit()
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

    fun logout() {
        firebaseAuth.signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }



    }