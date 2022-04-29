package com.example.rankmystore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CentralActivity : AppCompatActivity() {
    var signupButton: Button? = null
    var loginButton: Button? = null
    var reviewButton: Button? = null
    var searchButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central)

        // init view
        signupButton = findViewById(R.id.signupButton)
        loginButton = findViewById(R.id.loginButton)
        reviewButton = findViewById(R.id.reviewButton)
        searchButton = findViewById(R.id.searchButton)

        signupButton?.setOnClickListener({login()})
        loginButton?.setOnClickListener({signup()})
        reviewButton?.setOnClickListener({review()})
        searchButton?.setOnClickListener({search()})
    }

    private fun search() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun review() {
        startActivity(Intent(this,AddRatingActivity::class.java))
    }

    private fun login() {
        startActivity(Intent(this,LoginActivity::class.java))
    }

    private fun signup() {
        startActivity(Intent(this,SignUpActivity::class.java))
    }
}