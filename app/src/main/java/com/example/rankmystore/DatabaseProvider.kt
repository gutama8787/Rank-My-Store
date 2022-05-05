package com.example.rankmystore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class DatabaseProvider() {
    var mAuth: FirebaseAuth? = null
    var mDb: FirebaseFirestore? = null
    var mStr:FirebaseStorage? = null
    init {
        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()
        mStr = FirebaseStorage.getInstance()
    }

    // use to save text
    public fun saveData() {

    }

    // use to registerUser
    public fun singUp() {

    }
}