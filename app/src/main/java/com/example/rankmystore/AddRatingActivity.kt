package com.example.rankmystore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    val REQUEST_CODE_CAMERA = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rating)
        // initialize database and UI
        initDbAndUI()

        var comment = commentInputEditText!!.text!!.toString()
        var ratingValue = ratingBar!!.rating

        submit!!.setOnClickListener({ addRating() })

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
            Log.i("AddRatingActivity","Not logged in")
        }

        cameraButton!!.setOnClickListener({takePicture()})
    }

    private fun takePicture() {
        if (hasPermissionCamera()) {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_CODE_CAMERA)
            // setImageView()
        } else {
            requestPermissions(arrayOf((Manifest.permission.CAMERA)),4)
        }
    }

    private fun hasPermissionCamera(): Boolean {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 4) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),12)
            } else {
                Toast.makeText(this,"please grant permission to take picture",Toast.LENGTH_LONG ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            val img = data!!.extras!!.get("data") as Bitmap
            imageView!!.setImageBitmap(img)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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

    private fun isUserSignedIn(): Boolean {
        if (mAuth != null) {
            if (mAuth!!.currentUser != null) {
                Log.i(TAG,"${mAuth!!.currentUser!!.email}")
                return true
            }
            else {
                Log.i(TAG,"Log in or sign up first")
                submit?.setOnClickListener({Toast.makeText(this,"Login first!",Toast.LENGTH_LONG).show()})
            }
        }
        else {
            Log.i("AddRatingActivity","Not logged in")
        }
        return false
    }

    private fun addRating() {
        // var newRating = Rating("Address1","Address1","${mAuth!!.currentUser!!.email}","apple","picturepath")
        if (isUserSignedIn()) {
            var comment = commentInputEditText!!.text!!.toString()
            var ratingValue = ratingBar!!.rating

            var newExRating = RatingEx("${mAuth!!.currentUser!!.email}",ratingValue, comment)
            addData(newExRating)
        }

    }

    private fun readData() {
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

    private fun addData(newRating: RatingEx) {
// Add a new document with a generated ID
        Log.i(TAG,"adding to db...")
        db!!.collection("Rating")
            .add(newRating)
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

    class RatingEx(var user: String, var rate: Float, var commetn: String) {

    }
}