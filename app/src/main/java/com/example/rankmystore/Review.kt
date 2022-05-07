package com.example.rankmystore

class Review(var storeName: String, var storeLat: String,
             var storeLong:String, var contributor: String,
             var pictureId: String, var rating: Float, var comment: String) {
    constructor(storeLat: String,storeLong: String,contributor: String, pictureId:String,rating: Float) :
            this("",storeLat,storeLong,contributor,pictureId,rating,contributor){
        this.storeLat = storeLat
        this.storeLong = storeLong
        this.rating = rating
        this.pictureId = pictureId
        this.contributor = contributor
        this.pictureId = pictureId
    }

}