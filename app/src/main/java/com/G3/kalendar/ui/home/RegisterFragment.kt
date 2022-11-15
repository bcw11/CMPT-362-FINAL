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
import com.G3.kalendar.databinding.FragmentHomeBinding
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

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.btnCancel.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentRegister.removeAllViews()
            transaction.replace(R.id.FragmentRegister, HomeFragment())
            transaction.commit()
        }

        _binding!!.btnRegister.setOnClickListener{
            if(_binding!!.etName.text.isNotEmpty() && _binding!!.etEmail.text.isNotEmpty()
                && _binding!!.etPassword.text == _binding!!.etRepassword.text){

                    //save in sharedpreferences for now...


                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    _binding!!.FragmentRegister.removeAllViews()
                    transaction.replace(R.id.FragmentRegister, HomeFragment())
                    transaction.commit()
                 }
            else if(_binding!!.etPassword.text != _binding!!.etRepassword){
                val toast = Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_LONG)
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