package com.example.rankmystore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddRatingActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    var mAuth: FirebaseAuth? = null
    val TAG = "AddRatingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rating)
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
//        addData()
//        readData()
        if (mAuth != null) {
            if (mAuth!!.currentUser != null) {
                Log.i(TAG,"${mAuth!!.currentUser!!.email}")
            }
        }

        // addRating()
    }

    private fun addRating() {
        TODO("Not yet implemented")

    }

    private fun readData() {
        TODO("Not yet implemented")
        db!!.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun addData() {
        TODO("Not yet implemented")
        // Create a new user with a first and last name
        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815

// Add a new document with a generated ID

// Add a new document with a generated ID
        db!!.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

}