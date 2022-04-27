package com.example.rankmystore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class SignUpActivity : AppCompatActivity() {
    val TAG = "Signup Activity"
    var usernameInput: EditText? = null
    var passwordInput: EditText? = null
    var confirmpasswordInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val loginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)
        confirmpasswordInput = findViewById(R.id.password_confirm)

        loginButton.setOnClickListener({processLogin()})
        signupButton.setOnClickListener({processSignup()})

    }

    private fun processSignup() {
        Log.i(TAG,"login button clicked \n username is ${usernameInput?.text} password is ${passwordInput?.text}")

    }

    // sends user to the log in page if they already have account
    private fun processLogin() {
        val intent = Intent(
            this@SignUpActivity,
            LoginActivity::class.java
        )
        startActivity(intent)
    }
}