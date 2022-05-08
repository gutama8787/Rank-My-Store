
package com.example.rankmystore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddReviewActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    var mAuth: FirebaseAuth? = null
    var commentInputEditText: EditText? = null
    var imageView: ImageView? = null
    var cameraButton: ImageButton? = null
    var gallaryButton: ImageButton? = null
    var ratingBar: RatingBar? = null
    var submit: Button? = null
    var storeName: String? = ""
    var coordinates: String? = ""
    //    var dbProvider: DatabaseProvider? = null
    var fruitImg: Bitmap? = null
    var mImageUri: Uri? = null
    val dbProvider : DatabaseProvider = DatabaseProvider()
    val TAG = "AddRatingActivity"
    val REQUEST_CODE_CAMERA = 12
    val REQUEST_CODE_PICK_IMAGE = 55

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        // initialize database and UI
        storeName = intent.getStringExtra("STORE_NAME")
        coordinates = intent.getStringExtra("STORE_COORDINATES")

        initDbAndUI()
//        dbProvider = DatabaseProvider()
        var comment = commentInputEditText!!.text!!.toString()
        var ratingValue = ratingBar!!.rating

        submit!!.setOnClickListener({ addReview() })

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
            Log.i("AddReviewActivity","Not logged in")
        }

        cameraButton!!.setOnClickListener({takePicture()})
        gallaryButton!!.setOnClickListener({pickImage()})
    }

    private fun pickImage() {
//        TODO("Not yet implemented")
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,55)
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
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            val img = data!!.extras!!.get("data") as Bitmap
            imageView!!.setImageBitmap(img)
        } else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            var imgUri = data!!.data
            var mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
            fruitImg = mBitmap
            imageView!!.setImageBitmap(mBitmap)
        } else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            var imgUri = data!!.data
            var mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
            fruitImg = mBitmap
            imageView!!.setImageBitmap(mBitmap)
        }
    }

    private fun initDbAndUI() {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        commentInputEditText = findViewById(R.id.commentInputEditText)
        imageView = findViewById(R.id.imageView)
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

    private fun addReview() {
        // var newRating = Rating("Address1","Address1","${mAuth!!.currentUser!!.email}","apple","picturepath")

        if (isUserSignedIn()) {
            var comment = commentInputEditText!!.text!!.toString()
            var ratingValue = ratingBar!!.rating

            var review = Review(storeName!!,coordinates!!,ratingValue,comment,"fv9822795e-1fa5-4dd0-89b7-77d0133f19f5.jpg")
            Log.i(TAG,"Adding review in addReview")
            addData(review)
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

    private fun addData(review: Review) {
        // val uploadResult = dbProvider.uploadImage(fruitImg)
        // Log.i(TAG,"upload success $uploadResult")
        Log.i(TAG,"Adding review in addData")
// Add a new document with a generated ID
        Log.i(TAG,"adding to db...")
        db!!.collection("Review")
            .add(review)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                addImage()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun addImage() {
//        TODO("Not yet implemented")

    }
}