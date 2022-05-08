package com.example.rankmystore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView

//import com.google.common.io.Files.map

class StoreViewActivity: FragmentActivity(), OnMapReadyCallback {

    private lateinit var storeNameTextView: TextView
    private lateinit var adapter: panel_adapter
    private lateinit var leaveReviewBtn: Button
    lateinit var bottomTabs : BottomNavigationView

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_view)

        // The GoogleMap instance underlying the GoogleMapFragment defined in main.xml
        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        leaveReviewBtn = findViewById(R.id.leave_review_btn)
        bottomTabs = findViewById(R.id.bottom_navigation)
        storeNameTextView = findViewById(R.id.store_name)

        map.getMapAsync(this)

        val storeName = intent.getStringExtra("STORE_NAME")
        storeNameTextView.text = storeName

        var coordinates: String? = intent.getStringExtra("STORE_COORDINATES")
        var coordsArray: List<String?> = coordinates!!.split(",")
        lat = coordsArray[0]!!.toDouble()
        lng = coordsArray[1]!!.toDouble()

        leaveReviewBtn.setOnClickListener() {

            val leaveReviewIntent: Intent = Intent(this, AddReviewActivity::class.java)
            leaveReviewIntent.putExtra("STORE_COORDINATES", coordinates)
            leaveReviewIntent.putExtra("STORE_NAME", storeName)
            startActivity(leaveReviewIntent)

        }

        // Initialize the SDK
        val key = "AIzaSyCMEYURMvccFilVqreWH0j3Mi64cM2Zj5Y"
        Places.initialize(applicationContext, key)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)


        //get nearby places
        Log.d("storeList", "fragment size yee: " + MainActivity.storeList)
        val storeList = MainActivity.storeList

        //storeList.add(StoreEntry())

        adapter = panel_adapter(storeList)
        recyclerView.adapter = adapter



        //Tabs On Click Listener
        bottomTabs.setOnNavigationItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.page_1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
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

    override fun onMapReady(map: GoogleMap?) {

        val location = LatLng(lat, lng)




        if (map != null) {

            val options = GoogleMapOptions()

            val cameraPosition = CameraPosition.Builder()
                .target(location) // Sets the center of the map to Mountain View
                .zoom(17f)            // Sets the zoom
                .bearing(90f)         // Sets the orientation of the camera to east
                .tilt(50f)            // Sets the tilt of the camera to 30 degrees
                .build()              // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }



    }


}
