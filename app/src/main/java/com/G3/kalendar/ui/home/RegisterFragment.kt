package com.G3.kalendar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.user.User
import com.G3.kalendar.database.user.UserViewModel
import com.G3.kalendar.databinding.FragmentRegisterBinding


class RegisterFragment: Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPref = requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()

        //sets up all the database stuff for user
        val factory = DatabaseViewModelFactory("")
        val viewModel = ViewModelProvider(
            this,
            factory.userViewModelFactory
        ).get(UserViewModel::class.java)


        val loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.btnCancel.setOnClickListener {
            //testing

            //


            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentRegister.removeAllViews()
            transaction.replace(R.id.FragmentRegister, LoginFragment())
            transaction.commit()
        }

        _binding!!.btnRegister.setOnClickListener{
            //Can save user info into the database
            //check if email already exists in the database
            viewModel.users.observe(requireActivity()){
                for(user in it){
                    if(user.email.lowercase() == _binding!!.etEmail.text.toString().lowercase()){
                        val toast = Toast.makeText(context, "Password already exists in database", Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
            }

            //successful registration
            if(_binding!!.etName.text.isNotEmpty() && _binding!!.etEmail.text.isNotEmpty()
                && _binding!!.etPassword.text.toString() == _binding!!.etRepassword.text.toString()){

                var user = User()
                user.name = _binding!!.etName.text.toString()
                user.email = _binding!!.etEmail.text.toString()
                user.password = _binding!!.etPassword.text.toString()
                viewModel.insert(user)
                println("Debug registerfragment: user.name is " + user.name )
                println("Debug registerfragment: user.email is " + user.email)
                println("Debug registerfragment: user.password is " + user.password)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                _binding!!.FragmentRegister.removeAllViews()
                transaction.replace(R.id.FragmentRegister, LoginFragment())
                transaction.commit()
                 }
            else if(_binding!!.etPassword.text.toString() != _binding!!.etRepassword.text.toString()){
                val toast = Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_LONG)
                toast.show()
            }
            else{
                val toast = Toast.makeText(context, "Please fill out all the fields", Toast.LENGTH_LONG)
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