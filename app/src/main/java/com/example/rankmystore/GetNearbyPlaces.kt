package com.example.rankmystore

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class GetNearbyPlaces: AsyncTask<Object, String, String>() {
    lateinit var googlePlacesData: String
    lateinit var url: String


    override fun doInBackground(vararg v: Object): String {
        url = v[1] as String
        Log.d("bruh", "DoInBackGround url: " + url)

        var downloadUrl :DownloadUrl = DownloadUrl()
        try{
            googlePlacesData = downloadUrl.readUrl(url)
        }catch (e :IOException){
            e.printStackTrace()
        }

        Log.d("bruh", "googlePlacesData: " + googlePlacesData)
        return googlePlacesData
    }

    override fun onPostExecute(result: String) {
        var nearbyPlacesList : List<HashMap<String, String>>? = null
        var parser : DataParser = DataParser()
        Log.d("bruh", "GetNearbyPLaces onPostExec() url: " + result)
        nearbyPlacesList = parser.parse(result)
        showNearbyPlaces(nearbyPlacesList)
    }

    fun showNearbyPlaces(nearbyPlacesList: List<HashMap<String, String>>) {
        for(googlePlace in nearbyPlacesList){
            Log.d("bruh", "nearbyPlacesSize: " + nearbyPlacesList.size)
            Log.d("bruh", "googlePlace: " + googlePlace.size)
            var placeName : String? = googlePlace.get("place_name")
            var vicinity : String? = googlePlace.get("vicinity")
            var lat = googlePlace.get("lat")?.toDoubleOrNull()
            var lng = googlePlace.get("lng")?.toDoubleOrNull()
            var url = googlePlace.get("url")
            Log.d("store URL", url.toString())

            var store : StoreEntry = StoreEntry()

            if(placeName != null) {
                store.storeName = placeName
            }
            if (lat != null) {
                store.lat = lat
            }
            if (lng != null) {
                store.lng = lng
            }
            if(url != null){
                store.imageUrl = url
            }else{
                store.imageUrl = "https://purepng.com/public/uploads/large/big-chungus-jkg.png"
                Log.d("chungus", store.imageUrl)
            }
            if(vicinity != null){
                store.storeAddress = vicinity
            }

            (MainActivity.storeList as MutableList<StoreEntry>).add(store)
        }

        Log.d("storeList", "size: " + MainActivity.storeList.size)
    }

}