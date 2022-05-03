package com.example.rankmystore

import android.R.attr.password
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.trimmedLength
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignUpActivity : AppCompatActivity() {
    val TAG = "Signup Activity"
    var usernameInput: EditText? = null
    var emailInput: EditText? = null
    var passwordInput: EditText? = null
    var confirmpasswordInput: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)
        emailInput = findViewById(R.id.username)
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)
        confirmpasswordInput = findViewById(R.id.password_confirm)

        loginButton.setOnClickListener({processLogin()})
        signupButton.setOnClickListener({processSignup()})

    }

    private fun processSignup() {
        Log.i(TAG,"login button clicked \n username is ${usernameInput?.text} password is ${passwordInput?.text}")

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailInput!!.text).matches()

        var username = usernameInput!!.text.toString().trim()
        var password = passwordInput!!.text.toString().trim()
        var email = emailInput!!.text.toString().trim()
        var passwordConfirm = confirmpasswordInput!!.text.toString().trim()



        if (username.isNullOrEmpty()) {
            usernameInput?.setError("username must be at least 4 characters")
            return
        }
        else if (!isEmailValid) {
            emailInput?.setError("invalid email")
            return
        }
        else if (password.length < 5) {
            passwordInput?.setError("password must be at least 6 characters")
            return
        }
        else if (password != passwordConfirm) {
            confirmpasswordInput?.setError("Does not match password")
            return
        }
        else {
            createAccount(username,password)
        }

    }

    // sends user to the log in page if they already have account
    private fun processLogin() {
        val intent = Intent(
            this@SignUpActivity,
            LoginActivity::class.java
        )
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        // updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        var intent = Intent(this, MainActivity::class.java)
        Log.i(TAG, "${user!!.displayName}")
        startActivity(intent)
    }

    private fun createAccount(email:String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@SignUpActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

                // ...
            }
    }
}