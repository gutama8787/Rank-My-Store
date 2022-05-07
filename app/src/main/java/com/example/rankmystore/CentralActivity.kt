package com.example.rankmystore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class CentralActivity : AppCompatActivity() {

    var TAG = "CentralActivity"

    var signupButton: Button? = null
    var loginButton: Button? = null
    var reviewButton: Button? = null
    var searchButton: Button? = null
    var logoutButton: Button? = null
    var loggedIn: TextView? = null
    var auth: FirebaseAuth? = null
    var showAllStoresButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central)

        // init firebase
        auth = FirebaseAuth.getInstance()

        // init view
        signupButton = findViewById(R.id.signupButton)
        loginButton = findViewById(R.id.loginButton)
        reviewButton = findViewById(R.id.reviewButton)
        searchButton = findViewById(R.id.searchButton)
        logoutButton = findViewById(R.id.logoutButton)
        showAllStoresButton = findViewById(R.id.dbTester)

        signupButton?.setOnClickListener({login()})
        loginButton?.setOnClickListener({login()})
        reviewButton?.setOnClickListener({review()})
        searchButton?.setOnClickListener({search()})
        logoutButton?.setOnClickListener({logout()})
        showAllStoresButton?.setOnClickListener { showAllStores() }
        // set login status
        loginStatus()
    }

    private fun showAllStores() {
//        TODO("Not yet implemented")

    }

    private fun loginStatus() {
        // var text = loggedIn!!.text!!.toString()
        if (auth != null) {
            if (auth!!.currentUser != null) {
//                var text = loggedIn!!.text!!.toString()
//                loggedIn!!.text = text + auth!!.currentUser!!.email
                Toast.makeText(this,"You are logged in!",Toast.LENGTH_LONG)
            }
            else {
//                loggedIn!!.text = text + "None"
                Toast.makeText(this,"You are not logged in!",Toast.LENGTH_LONG)
            }
        }
        else {
            Log.i(TAG,"no auth")
        }
    }

    private fun logout() {
        if (auth != null) {
            if (auth!!.currentUser != null) {
                Log.i(TAG, "signing out : ${auth!!.currentUser!!.email}")
                auth?.signOut()
                loginStatus()
            }
            else {
                Log.i(TAG,"no current user")
            }
        }
        else {
            Log.i(TAG,"no auth")
        }
        // startActivity(Intent(this,MainActivity::class.java))
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