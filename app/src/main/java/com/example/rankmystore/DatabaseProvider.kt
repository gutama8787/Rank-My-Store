package com.example.rankmystore

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.Thread.sleep
import java.util.*


/**
 * This class sets an instance of firebase db's used by our app/
 * Supports
 * - registering users
 * - logging in users.
 * - storing user ratings with store information
 * - storing fruit pictures
 *
 */

class DatabaseProvider {
    var mAuth: FirebaseAuth? = null
    var mDb: FirebaseFirestore? = null
    var mStr:FirebaseStorage? = null
    val TAG = "DatabaseProvider"
    init {
        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()
        mStr = FirebaseStorage.getInstance()
    }


    // register user
    public fun registerUser() {

    }

    public fun loginUser(email: String, password: String) {

    }

    // adds store to the database.
    public fun saveStore(store: Store) {
        Log.i(TAG,"adding to db...")
        var ratingId: String?  = null
        mDb!!.collection("Store")
            .add(store)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    // save rating
    public fun saveReview(review: Review, fruitImage: Bitmap) {
        // save the image in firebase storage associate it with the id of the image.
        // store the Rating in fireStore and associate it with the id of the rating
        Log.i(TAG,"adding to db...")
        var ratingId: String?  = null
        mDb!!.collection("Review")
            .add(review)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }
    public fun uploadImage(bitmap: Bitmap?) {
        // Create a storage reference from our app
        val storageRef = mStr!!.reference

        val uuid = UUID.randomUUID().toString()

        // Create a reference to "mountains.jpg"
        val fruitVegRef = storageRef.child("fv$uuid.jpg")

        // Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/fv$uuid.jpg")

        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = fruitVegRef.putBytes(data)
        var v = uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.i("add","success upload")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.i("add","failed upload")
        }
    }
    // get current user
    public fun getCurrentUserEmail() {

    }

    // returns list of stores with their average rating value, address, and image.
    public fun getAllStores() {
        mDb!!.collection("Store")
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

    // get ratings for specific store provided store name and address
    //
    public fun getStoreRatings(name :String, address: String, ratingBar: RatingBar,
                               textView: TextView, isForAvg: Boolean) : ArrayList<Float>{
        var ratings: ArrayList<Float> = ArrayList<Float>()
        var sum = 0.0f
        var avg = 0.0f
        var x = mDb!!.collection("Review")
            .whereEqualTo("coordinates",address)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        ratings.add(((document.data["rating"] as Double).toFloat()))
                        Log.d(TAG, "ratings size: " + ratings.size)
                        Log.d(TAG, document.id + " => " + (document.data["rating"] as Double).toFloat())

                        sum += (document.data["rating"] as Double).toFloat()
                    }

                    if (isForAvg == true) {
                        if (ratings.size != 0) {
                            avg = (sum / ratings.size)
                            ratingBar.rating = avg
                        } else {
                            ratingBar.rating = 5.0f
                        }
                    } else if (ratings.size != 0){
                        textView.text = ratings.size.toString() + " review(s)"
                    } else {
                        textView.text = "No reviews"
                    }

                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }

        return ratings
    }
}

