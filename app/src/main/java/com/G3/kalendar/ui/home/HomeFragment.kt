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
import com.G3.kalendar.R
import com.G3.kalendar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    public val label = "Home"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val sharedPref = requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        _binding!!.btnRegister.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentHome.removeAllViews()
            transaction.replace(R.id.FragmentHome, RegisterFragment())
            transaction.commit()
        }

        _binding!!.btnLogin.setOnClickListener{
            var storedName = sharedPref.getString("Name","").toString()
            var storedEmail = sharedPref.getString("Email","").toString()
            var storedPassword = sharedPref.getString("Password","").toString()

            var enteredEmail = _binding!!.etEmail.text.toString()
            var enteredPassword = _binding!!.etPassword.text.toString()

            if(enteredEmail.isEmpty() || enteredPassword.isEmpty()){
                val toast = Toast.makeText(context, "Incorrect email or password. Empty", Toast.LENGTH_LONG)
                toast.show()
            }

            else if(enteredEmail == storedEmail && enteredPassword == storedPassword){
                val toast = Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG)
                toast.show()
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


