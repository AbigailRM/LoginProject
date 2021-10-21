package com.moviledev.login.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.moviledev.login.R
import com.moviledev.login.databinding.FragmentForgotPassBinding

class ForgotPassFragment : Fragment() {

    private lateinit var binding: FragmentForgotPassBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_pass, container, false)

        //init firebase auth
        auth = FirebaseAuth.getInstance()

        binding.btnRestorePass.setOnClickListener {
            validateEmail()
        }

        binding.tvAccount.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_view_fragment, LoginFragment()).commit()
        }

        return binding.root
    }

    private var email=""

    private fun validateEmail() {
        email = binding.etEmail.text.toString().trim()

        //validated email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()){
            Toast.makeText(requireActivity(), "Invalid Email Pattern Or None Email Is Empty...", Toast.LENGTH_SHORT).show()
        }
        else(
                restorePass(email)
        )
    }

    private fun restorePass(email: String) {
        val emailAddress = email

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Toast.makeText(requireActivity(), "Email sent successfully to reset your password!", Toast.LENGTH_SHORT).show()

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()

                    transaction.replace(R.id.frame_view_fragment, LoginFragment()).commit()
                }else{
                    Toast.makeText(requireActivity(), task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()

                }
            }
    }

}