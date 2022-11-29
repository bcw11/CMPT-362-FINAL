package com.G3.kalendar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.R
import com.G3.kalendar.database.DatabaseViewModelFactory
import com.G3.kalendar.database.user.User
import com.G3.kalendar.database.user.UserViewModel
import com.G3.kalendar.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment: Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var enteredPassword: String
    private lateinit var reTypedPassword: String
    private lateinit var usersList: List<User>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        var userEmail = arguments?.getString("Email")
        println("Debug: userEmail is " + userEmail)
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = DatabaseViewModelFactory("")
        val viewModel = ViewModelProvider(
            this,
            factory.userViewModelFactory
        ).get(UserViewModel::class.java)

        _binding!!.btnChangePassword.setOnClickListener{
            enteredPassword = _binding!!.etPassword.text.toString()
            reTypedPassword = _binding!!.etRetypePassword.text.toString()

            for (user in viewModel.users.value!!) {
                if (user.email == userEmail) {
                    viewModel.changePassword(user.id, enteredPassword)
                }
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                _binding!!.FragmentChangePassword.removeAllViews()
                transaction.replace(R.id.FragmentRecover, HomeFragment())
                transaction.commit()
            }
        }

        _binding!!.btnCancel.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentChangePassword.removeAllViews()
            transaction.replace(R.id.FragmentRecover, HomeFragment())
            transaction.commit()
        }


        return root
    }

}