package com.G3.kalendar

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.G3.kalendar.databinding.ActivityMainBinding
import com.G3.kalendar.ui.calendar.CalendarFragment
import com.G3.kalendar.ui.home.HomeFragment
import com.G3.kalendar.ui.kanban.KanbanFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_kanban, R.id.nav_calendar), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // getting current fragment label
        var currentLabel = navController.currentDestination?.label

        // setting button to be visible
//        if(currentLabel == HomeFragment().label){ binding.appBarMain.fab.visibility = View.GONE }
//        else{ binding.appBarMain.fab.visibility = View.VISIBLE }

        // adapted from https://stackoverflow.com/questions/67147901/kotlin-opening-new-fragment-in-nav-drawer-example
        // switching icons https://stackoverflow.com/questions/15052669/android-change-button-icon-when-clicked
        // switches between calendar and kanban fragments
        binding.appBarMain.fab.setOnClickListener {
            currentLabel = navController.currentDestination?.label
            // finding current fragment
//            if(currentLabel == CalendarFragment().label){
////                binding.appBarMain.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_slideshow))
//                navController.navigate(R.id.nav_kanban)
//            }
//            if(currentLabel == KanbanFragment().label){
////                binding.appBarMain.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_gallery))
//                navController.navigate(R.id.nav_calendar)
//            }
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}