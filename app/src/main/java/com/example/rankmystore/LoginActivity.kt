package com.example.rankmystore

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * TODO Sign in with Google: https://firebase.google.com/docs/auth/android/google-signin#kotlin+ktx_1
 */

class LoginActivity : AppCompatActivity() {
    val TAG = "Login-Activity"

    var usernameInput: EditText? = null
    var passwordInput: EditText? = null
    var auth: FirebaseAuth? = null
    lateinit var bottomTabs : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        bottomTabs = findViewById(R.id.bottom_navigation)



        val loginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)

        loginButton.setOnClickListener({ processLogin() })
        signupButton.setOnClickListener({ processSignup() })

        bottomTabs.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
//                    searchBar.setText("")
//                    searchBar.performClick()
                    false
                }
                R.id.page_2 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.page_3 -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }

        }
    }

    private fun processSignup() {


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
        if (usernameInput == null || passwordInput == null) {
            return
        }
        else if (usernameInput!!.text == null || passwordInput?.text == null) {
            usernameInput!!.setError("Can't be empty")
            return
        }
        else if ((usernameInput!!.text!!.toString().isNullOrEmpty() || passwordInput?.text!!.toString().isNullOrEmpty())) {
            usernameInput!!.setError("Can't be empty")
            return
        }


        var email = usernameInput?.text
        Log.i(TAG,"sign up clicked")
        if (auth != null) {
            auth!!.signInWithEmailAndPassword(usernameInput!!.text.toString(),passwordInput!!.text.toString())
                .addOnCompleteListener(this) { task ->
                    Log.i(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)

                    if (!task.isSuccessful) {
                        Log.i(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                    }
                }
        }
    }
}