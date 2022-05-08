package com.example.rankmystore

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ReviewActivity : AppCompatActivity() {
    var editTextTextPersonName: EditText? = null
    var search: Button? = null
    private lateinit var adapter: ReviewAdapter
    lateinit var bottomTabs : BottomNavigationView
    var storeNameTextView: TextView? = null
    var db: FirebaseFirestore? = null
    var mAuth: FirebaseAuth? = null
    var mStr: FirebaseStorage? = null
    var TAG = "add"
    var reviewList: ArrayList<StoreEntry>? = ArrayList<StoreEntry>()
    var storeCoord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        mStr = FirebaseStorage.getInstance()

//        var storeName = intent.getStringExtra("STORE_NAME")
//        var coordinates = intent.getStringExtra("STORE_COORDINATES")
        storeCoord = intent.getStringExtra("STORE_COORDINATES")

        bottomTabs = findViewById(R.id.bottom_navigation)
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)
        search = findViewById(R.id.search)

        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)


        val storeList = reviewList // reviewList
        //storeList.add(StoreEntry())
        adapter = ReviewAdapter(storeList as List<StoreEntry>)
        recyclerView.adapter = adapter

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

    private fun displayReviews() {
        // TODO("Not yet implemented")
        var name = editTextTextPersonName!!.text!!.toString()

        db!!.collection("Review")
            .whereEqualTo("coordinates",storeCoord)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.i(TAG,"${document.id}")
                        // get an image for each document

                        val storageRef = mStr!!.reference

                        // Create a reference with an initial file path and name
                        val pathReference = storageRef.child("images/${document.id}.jpeg")

                        Log.i("add","going to load image for images/${document.id}.jpeg")

                        val httpsReference = mStr!!.getReferenceFromUrl(
                            "gs://rankmystore.appspot.com/${document.id}.jpg")

                        val ONE_MEGABYTE: Long = 1024 * 1024
                        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                            // Data for "images/island.jpg" is returned, use this as needed
                            var img = BitmapFactory.decodeByteArray(it,0, it.size)

                            // picImageView!!.setImageBitmap(img)
                            Log.i("add","stored imag")
                            var comment = document.data["comment"] as String
                            var storeName = document.data["storeName"] as String
                            var rating = (document.data["rating"] as Double).toFloat()

                            var review = StoreEntry()
                            review.storeName = storeName
                            review.storeRating = rating.toString()
                            review.comment = comment
                            review.img = img

                            reviewList!!.add(review)
                            adapter.notifyDataSetChanged()

                        }.addOnFailureListener {
                            // Handle any errors
                            Log.i("add","failed to load $it")
                        }

                        // image gotten

                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
}

