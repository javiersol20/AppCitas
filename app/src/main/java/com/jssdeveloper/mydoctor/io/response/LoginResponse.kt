package com.jssdeveloper.mydoctor.io.response

import com.jssdeveloper.mydoctor.model.User

data class LoginResponse (val success: Boolean, val user: User, val jwt: String)