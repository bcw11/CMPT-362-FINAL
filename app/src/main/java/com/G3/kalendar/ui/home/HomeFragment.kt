package com.G3.kalendar.ui.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.G3.kalendar.R
import com.G3.kalendar.database.user.UserDao
import com.G3.kalendar.database.user.UserRepository
import com.G3.kalendar.database.user.UserViewModel
import com.G3.kalendar.database.user.UserViewModelFactory
import com.G3.kalendar.databinding.FragmentHomeBinding
import com.G3.kalendar.ui.calendar.CalendarFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var navController: NavController
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    public val label = "Home"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //sets up all the database stuff for user
        val db = Firebase.firestore
        val dao = UserDao(db)
        val repository = UserRepository(dao)
        val viewModelFactory = UserViewModelFactory(repository)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(UserViewModel::class.java)

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val sharedPref = requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        _binding!!.tvRecover.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentHome.removeAllViews()
            transaction.replace(R.id.FragmentHome, RecoverFragment())
            transaction.commit()
        }

        _binding!!.btnRegister.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentHome.removeAllViews()
            transaction.replace(R.id.FragmentHome, RegisterFragment())
            transaction.commit()
        }

        _binding!!.btnLogin.setOnClickListener{

            var enteredEmail = _binding!!.etEmail.text.toString()
            var enteredPassword = _binding!!.etPassword.text.toString()
            val foundUser = viewModel.authenticate(enteredEmail,enteredPassword)

            if(enteredEmail.isEmpty() || enteredPassword.isEmpty()){
                val toast = Toast.makeText(context, "Incorrect email or password. Empty", Toast.LENGTH_LONG)
                toast.show()
            }

            else if(foundUser != null){
                val toast = Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG)
                toast.show()
                _binding!!.FragmentHome.removeAllViews()
                val navigationView = requireActivity().findViewById<View>(R.id.nav_view) as NavigationView
                navigationView.menu.getItem(2).isChecked = true
               /*val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.FragmentHome, CalendarFragment())
                transaction.commit()*/
                navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_kanban)
            }

            else{
                val toast = Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_LONG)
                toast.show()
            }

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


