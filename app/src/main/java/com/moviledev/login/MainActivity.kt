package com.moviledev.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.moviledev.login.Models.LoginViewModel
import com.moviledev.login.databinding.ActivityMainBinding
import com.moviledev.login.fragments.LoginFragment
import com.moviledev.login.fragments.ProfileFragment
import com.moviledev.login.fragments.SignUpFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private val model = ViewModelProvider(this).get(LoginViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //binding.btnLogin.setOnClickListener {
            fragmentTransaction()

        //}
    }

    private fun fragmentTransaction() {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.frame_view_fragment, LoginFragment()).commit()
    }


}