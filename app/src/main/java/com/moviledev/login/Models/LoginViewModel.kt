package com.moviledev.login.Models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.moviledev.login.FirebaseUserLiveData

class LoginViewModel : ViewModel() {

    enum class AuthenticationState{
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map{ user ->
        if (user != null){
            AuthenticationState.AUTHENTICATED
        }
        else{
            AuthenticationState.UNAUTHENTICATED
        }

    }

}