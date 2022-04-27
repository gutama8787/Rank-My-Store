package com.example.rankmystore

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    val TAG = "Login-Activity"

    var usernameInput: EditText? = null
    var passwordInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val loginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)

        loginButton.setOnClickListener({processLogin()})
        signupButton.setOnClickListener({processSignup()})


    }

    private fun processSignup() {
        Log.i(TAG,"sign up clicked")

        // open a new activity
        // create an intent to start SignUpActivity
        val intent = Intent(
            this@LoginActivity,
            SignUpActivity::class.java
        )
        startActivity(intent)
    }

    private fun processLogin() {
        Log.i(TAG,"login button clicked \n username is ${usernameInput?.text} password is ${passwordInput?.text}")

    }
}