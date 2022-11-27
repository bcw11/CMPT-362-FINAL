package com.G3.kalendar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.R
import com.G3.kalendar.database.user.UserDao
import com.G3.kalendar.database.user.UserRepository
import com.G3.kalendar.database.user.UserViewModel
import com.G3.kalendar.database.user.UserViewModelFactory
import com.G3.kalendar.databinding.FragmentChangePasswordBinding
import com.G3.kalendar.databinding.FragmentRecoverBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChangePasswordFragment: Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var enteredPassword: String
    private lateinit var reTypedPassword: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = Firebase.firestore
        val dao = UserDao(db)
        val repository = UserRepository(dao)
        val viewModelFactory = UserViewModelFactory(repository)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(UserViewModel::class.java)

        _binding!!.btnChangePassword.setOnClickListener{
            enteredPassword = _binding!!.etPassword.text.toString()
            reTypedPassword = _binding!!.etRetypePassword.text.toString()

            if(enteredPassword == reTypedPassword){

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