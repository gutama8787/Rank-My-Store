package com.example.rankmystore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Transformations.map
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
//import com.google.common.io.Files.map

class StoreViewActivity: AppCompatActivity(), OnMapReadyCallback {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_view)

        // The GoogleMap instance underlying the GoogleMapFragment defined in main.xml
        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)


    }


    override fun onMapReady(map: GoogleMap?) {

        if (map != null) {

            // Set the map position
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        29.0,
                        -88.0
                    ), 3.0f
                )
            )

            // Add a marker on Washington, DC, USA
            map.addMarker(
                MarkerOptions().position(
                    LatLng(38.8895, -77.0352)
                ).title(
                    "hi"
                )
            )

            // Add a marker on Mexico City, Mexico
            map.addMarker(
                MarkerOptions().position(
                    LatLng(19.13, -99.4)
                ).title(
                    "hi"
                )
            )
        }
    }


}