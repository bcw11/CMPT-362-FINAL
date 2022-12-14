package com.G3.kalendar.ui.home

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.G3.kalendar.MainActivity
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.user.UserViewModel
import com.G3.kalendar.databinding.ActivityHomeBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    // This property is only valid between onCreateView and
    // onDestroyView.

    public val label = "Home"
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        checkAlarmPermission()

        val sharedPref = this.getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        //sets up all the database stuff for user

        val factory = DatabaseViewModelFactory("")
        val viewModel = ViewModelProvider(
            this,
            factory.userViewModelFactory
        ).get(UserViewModel::class.java)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root


        if(sharedPref.getString("id","") != "" ){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ActivityHome,LoginFragment())
        transaction.commit()
    }

    // Adapted from: https://stackoverflow.com/questions/71031091/android-12-using-schedule-exact-alarm-permission-to-get-show-data-at-specific-t
    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(this, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    startActivity(intent)
                }
            }
        }
    }
}


