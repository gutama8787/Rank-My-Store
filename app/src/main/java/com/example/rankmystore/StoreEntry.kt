package com.example.rankmystore

import android.graphics.Bitmap
import android.os.Bundle

class StoreEntry {
    var storeName = ""
    var storeRating = "5.0"
    var imageUrl: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
    var comment: String = ""
    var img: Bitmap? = null
}