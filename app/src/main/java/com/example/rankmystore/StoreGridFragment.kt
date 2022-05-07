package com.example.rankmystore

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest


class StoreGridFragment: Fragment() {
    var PROXIMITY_RADIUS = 10000
    var longitude: Double = 76.6100414
    var latitude: Double = 39.1902558

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment with the StoreGrid theme
        val view = inflater.inflate(R.layout.store_scroll_fragment, container, false)

        // Set up the RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)


        //get nearby places
        Log.d("storeList", "fragment size: " + MainActivity.storeList)
        val storeList = MainActivity.storeList


        val adapter = panel_adapter(storeList)
        recyclerView.adapter = adapter

        return view
    }

    private fun getUrl(latitude: Double, longitude : Double, nearbyPlace : String): String{
        var googlePlaceUrl : StringBuilder = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlaceUrl.append("location=" + latitude + "," + longitude)
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS)
        googlePlaceUrl.append("&type=" + nearbyPlace)
        googlePlaceUrl.append("&sensor=true")
        googlePlaceUrl.append("&key=" + "AIzaSyBUTnijferzRn5Scrgm5tgdx_FMyAej264")

        return googlePlaceUrl.toString()
    }

    companion object{
        private const val label = "placeholder label"
        private const val requestCode = 100

    }


}