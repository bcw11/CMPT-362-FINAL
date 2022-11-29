package com.G3.kalendar.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.G3.kalendar.MainActivity
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.user.UserViewModel
import com.G3.kalendar.databinding.FragmentLoginBinding

class LoginFragment : Fragment(){
    private var _binding: FragmentLoginBinding? = null
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
        val factory = DatabaseViewModelFactory("")
        val viewModel = ViewModelProvider(
            this,
            factory.userViewModelFactory
        ).get(UserViewModel::class.java)

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPref = requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()

        if(sharedPref.getString("id","") != "" ){
            _binding!!.FragmentLogin.removeAllViews()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        _binding!!.tvRecover.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentLogin.removeAllViews()
            transaction.replace(R.id.FragmentLogin, RecoverFragment())
            transaction.commit()
        }

        _binding!!.btnRegister.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentLogin.removeAllViews()
            transaction.replace(R.id.FragmentLogin, RegisterFragment())
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
                println("DEBUG: foundUser ID is " + foundUser.id)
                toast.show()

                editor.putString("id", foundUser.id)
                editor.apply()
                editor.commit()
                _binding!!.FragmentLogin.removeAllViews()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            else{
                val toast = Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_LONG)
                toast.show()
            }

        }
        return root
    }
}