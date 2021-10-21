package com.moviledev.login

data class User(
    var name : String ?= null,
    var lastname: String ?= null,
    var sex: String ?= null,
    var dateOfBirth: String ?= null,
    var email: String ?= null,
    var phone: String ?= null
)
