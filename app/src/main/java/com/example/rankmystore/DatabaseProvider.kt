package com.example.rankmystore

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.Thread.sleep
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt


/**
 * This class sets an instance of firebase db's used by our app/
 * Supports
 * - logging in users.
 * - gets information from db as well
 * This is are common functionalities for most activities.
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

            Thread.sleep(1000)
        return ratings
    }


    public fun isUserSignedIn(): Boolean {
        if (mAuth != null) {
            if (mAuth!!.currentUser != null) {
                Log.i(TAG,"${mAuth!!.currentUser!!.email}")
                return true
            }
            else {
                Log.i(TAG,"Log in or sign up first")
                return false
            }
        }
        else {
            Log.i("AddRatingActivity","Not logged in")
        }
        return false
    }
    public fun getCardAvgRating(address: String, textView: TextView) : Float{
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

                    if(sum.compareTo(0.0) != 0) {
                        avg = (sum / ratings.size)
                        val roundoff = (avg * 100.0).roundToInt() / 100.0
                        textView.setText("Rating: " + roundoff)
                    }else {
                        textView.setText("No Reviews yet!")
                    }


                } else {
                    textView.setText("No Reviews yet!")
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }

        Thread.sleep(1000)
        return avg

    }
}

