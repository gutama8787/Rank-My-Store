package com.example.rankmystore

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddRatingActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    var mAuth: FirebaseAuth? = null
    var commentInputEditText: EditText? = null
    var imageView: ImageView? = null
    var addressInputEditText: EditText? = null
    var cameraButton: ImageButton? = null
    var gallaryButton: ImageButton? = null
    var ratingBar: RatingBar? = null
    var submit: Button? = null

    val TAG = "AddRatingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rating)
        // initialize database and UI
        initDbAndUI()



//        addData()
//        readData()
        if (mAuth != null) {
            if (mAuth!!.currentUser != null) {
                Log.i(TAG,"${mAuth!!.currentUser!!.email}")
            }
            else {
                Log.i(TAG,"Log in or sign up first")
                submit?.setOnClickListener({Toast.makeText(this,"Login first!",Toast.LENGTH_LONG).show()})
            }
        }
        else {

        }

        // addRating()
    }

    private fun initDbAndUI() {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        commentInputEditText = findViewById(R.id.commentInputEditText)
        imageView = findViewById(R.id.imageView)
        addressInputEditText = findViewById(R.id.addressInputEditText)
        cameraButton = findViewById(R.id.cameraButton)
        gallaryButton = findViewById(R.id.gallaryButton)
        ratingBar = findViewById(R.id.ratingBar)
        submit = findViewById(R.id.submit)

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

    // Users add ratings
    // rating has, rating value, store name, and user who rated it.
    // Rating class

    /***
     * PictureID:
     * We can't store large files in firestore, therefore, we will use firebase storage to storegae to do that.
     * Then associate the the pictureID in storage to firestore.
     * contributor is a person who adds the rating.
     *
     */
    class Rating(var storeName: String, var storeAddress: String, var contributor: String, var fruitName: String, var pictureId: String) {

    }
}