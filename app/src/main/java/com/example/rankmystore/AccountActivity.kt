package com.example.rankmystore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

    var logoutButton: Button? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        mAuth = FirebaseAuth.getInstance()
        logoutButton = findViewById(R.id.logoutButton)
        logoutButton!!.setOnClickListener({logout()})

    }

    private fun logout() {
//        TODO("Not yet implemented")
        mAuth!!.signOut()
    }
}