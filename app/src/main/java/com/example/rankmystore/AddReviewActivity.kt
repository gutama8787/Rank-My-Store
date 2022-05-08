
package com.example.rankmystore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class AddReviewActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    var mAuth: FirebaseAuth? = null
    var mStr: FirebaseStorage? = null
    var commentInputEditText: EditText? = null
    var imageView: ImageView? = null
    var cameraButton: ImageButton? = null
    var gallaryButton: ImageButton? = null
    var ratingBar: RatingBar? = null
    var progressBar: ProgressBar? = null
    var submit: Button? = null
    lateinit var bottomTabs : BottomNavigationView

    var fruitImg: Bitmap? = null
    var mImageUri: Uri? = null
    val dbProvider : DatabaseProvider = DatabaseProvider()
    val TAG = "AddRatingActivity"
    val REQUEST_CODE_CAMERA = 12
    val REQUEST_CODE_PICK_IMAGE = 55

    var storeName: String? = null
    var coordinates: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        // initialize database and UI
        storeName = intent.getStringExtra("STORE_NAME")
        coordinates = intent.getStringExtra("STORE_COORDINATES")
        bottomTabs = findViewById(R.id.bottom_navigation)

        initDbAndUI()

        mStr = FirebaseStorage.getInstance()
        submit!!.setOnClickListener( View.OnClickListener { view ->
            progressBar!!.setVisibility(View.VISIBLE)
            addReview()
        })

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

        bottomTabs.setOnNavigationItemSelectedListener {
                item ->
            when(item.itemId){
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
            fruitImg = img
            imageView!!.setImageBitmap(img)
        } else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            var imgUri = data!!.data
            var mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
            Log.i("add","image is being set ..")
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
        progressBar = findViewById(R.id.progressBar)
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
            finish()
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

        Log.i(TAG,"adding to db...")
        db!!.collection("Review")
            .add(review)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                Toast.makeText(this,"rating added",Toast.LENGTH_LONG)
                uploadImage(documentReference.id)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    fun uploadImage(id: String) {

        if (fruitImg == null) {
            Log.i("add","image is null!!")
            Toast.makeText(this,"Image not provided",Toast.LENGTH_LONG)
            return
        }
        // Create a storage reference from our app
        val storageRef = mStr!!.reference

        val uuid = UUID.randomUUID().toString()

        // Create a reference to "mountains.jpg"
        val fruitVegRef = storageRef.child("$id.jpg")

        // Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/$id.jpg")

        val baos = ByteArrayOutputStream()
        fruitImg!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = fruitVegRef.putBytes(data)
        Log.i("add","uploading image!!")
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.i("add","failed to upload")
            Toast.makeText(this,"failed to uploaded",Toast.LENGTH_LONG)
            progressBar!!.isVisible = false
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.i("add","successfully upload")
            // progressBar!!.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"successfully uploaded",Toast.LENGTH_LONG)
            progressBar!!.isVisible = false
            finish()
        }
    }
}