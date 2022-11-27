package com.G3.kalendar.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.G3.kalendar.R
import com.G3.kalendar.databinding.FragmentRecoverBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.properties.Delegates

class RecoverFragment: Fragment() {
    private var _binding: FragmentRecoverBinding? = null

    private val binding get() = _binding!!
    private lateinit var enteredPIN: String
    private var PIN by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentRecoverBinding.inflate(inflater, container, false)
        val root: View = binding.root


        _binding!!.btnSendCode.setOnClickListener{
            val toast = Toast.makeText(context, "A PIN has been sent to the email you entered", Toast.LENGTH_LONG)
            toast.show()
            //email is for testing purposes
            val rnd = Random()
            PIN = rnd.nextInt(999999)
            val stringPIN = String.format("%06d", PIN)
            var email = _binding!!.etEmail.text.toString()
            println("Debug recoverfragment: PIN is" + PIN.toString())
            Mailer.sendMail(
                email,
                "Kalendar - Reset Your Password",
                "Hi, \nYou are receiving this email because you requested to reset your password\nYour PIN is: " + stringPIN
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Toast.makeText(context, "Email sent!", Toast.LENGTH_LONG).show()
                    }
                )
        }

        //let user recover account
        _binding!!.btnRecoverAccount.setOnClickListener{
            enteredPIN = _binding!!.etPin.text.toString()
            println("Debug recoverfragment: enteredPIN is" + enteredPIN)
            if(PIN.toString() == enteredPIN){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                _binding!!.FragmentRecover.removeAllViews()
                transaction.replace(R.id.FragmentRecover, ChangePasswordFragment())
                transaction.commit()
            }
        }

        _binding!!.btnCancel.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            _binding!!.FragmentRecover.removeAllViews()
            transaction.replace(R.id.FragmentRecover, HomeFragment())
            transaction.commit()
        }
        return root
    }

}