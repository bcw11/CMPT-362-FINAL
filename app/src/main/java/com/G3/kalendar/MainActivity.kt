package com.G3.kalendar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.G3.kalendar.databinding.ActivityMainBinding
import com.G3.kalendar.ui.calendar.CalendarFragment
import com.G3.kalendar.ui.home.LoginActivity
import com.G3.kalendar.ui.kanban.KanbanFragment
import com.G3.kalendar.ui.stats.StatsActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // turning off night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // setting view and action bar
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        // setting navigation config
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_kanban, R.id.nav_calendar), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // set start destination
        navController = this.findNavController(R.id.nav_host_fragment_content_main)
        var navGraph: NavGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
        if(sharedPref.getString("id", "") != "") {
            navGraph.setStartDestination(R.id.nav_kanban)
            binding.appBarMain.switchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_calendar))
        }
       navController.graph = navGraph

        // fixed navigation menu bug
        navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.nav_calendar -> {
                    binding.appBarMain.switchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_kanban))
                    navController.navigate(R.id.nav_calendar)
                }
                R.id.nav_kanban -> {
                    binding.appBarMain.switchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_calendar))
                    navController.navigate(R.id.nav_kanban)
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            false
        }

        // adapted from https://stackoverflow.com/questions/67147901/kotlin-opening-new-fragment-in-nav-drawer-example
        // switching icons https://stackoverflow.com/questions/15052669/android-change-button-icon-when-clicked
        // switches between calendar and kanban fragments
        binding.appBarMain.switchFab.setOnClickListener {
            var currentLabel = navController.currentDestination?.label

            // finding current fragment
            if(currentLabel == CalendarFragment().label){
                binding.appBarMain.switchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_calendar))
                navController.navigate(R.id.nav_kanban)
            }
            if(currentLabel == KanbanFragment().label){
                binding.appBarMain.switchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_kanban))
                navController.navigate(R.id.nav_calendar)
            }
        }

        // adds epics and stories
        binding.appBarMain.addFabStory.setOnClickListener {
            val intent = Intent(this, AddTicket::class.java)
            startActivity(intent)
        }

        binding.appBarMain.addFabEpic.setOnClickListener {
            val intent = Intent(this, AddEpicActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_stats->{
                //intent
                val intent = Intent(this, StatsActivity::class.java)
                startActivity(intent)
            }

            R.id.action_logout-> {
                val sharedPref = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.clear()
                editor.commit()

                val navigationView = this.findViewById<View>(R.id.nav_view) as NavigationView
                navigationView.menu.getItem(0).isChecked = true
                navController = this.findNavController(R.id.nav_host_fragment_content_main)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



}
