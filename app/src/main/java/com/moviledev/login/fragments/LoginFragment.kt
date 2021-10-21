package com.moviledev.login.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.moviledev.login.Models.LoginViewModel
import com.moviledev.login.R
import com.moviledev.login.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    //Progress dialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var auth: FirebaseAuth

    //private val model = ViewModelProvider(this).get(LoginViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        //init firebase auth
        auth = FirebaseAuth.getInstance()

        //init progress dialog
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.tvNoAccount.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_view_fragment, SignUpFragment()).commit()
        }

        binding.tvForgotPass.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_view_fragment, ForgotPassFragment()).commit()
        }

        binding.btnSubmit.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private var email = ""
    private var password = ""

    private fun validateData() {
        //input data
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim {it <= ' '}

        //validated data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()){
            Toast.makeText(requireActivity(), "Invalid Email Pattern...", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(requireActivity(), "Enter password", Toast.LENGTH_SHORT).show()
        }
        else{
            loginUser()
        }
    }

    private fun loginUser() {

        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        //Toast.makeText(requireActivity(), "Enter password ${binding.etPassword.toString().trim()} " +
                //"and ${binding.etPassword.toString().trim()}", Toast.LENGTH_SHORT).show()

        //email = "mediplussystem@gmail.com"
        //password = binding.etPassword.toString().trim {it <= ' '}

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()

                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.frame_view_fragment, ProfileFragment()).commit()

            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

}