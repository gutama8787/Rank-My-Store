package com.example.rankmystore

import android.R.attr.apiKey
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var searchBar: TextInputEditText
    lateinit var searchButton : Button
    lateinit var bottomTabs : BottomNavigationView
    var request_code = 101
    var PROXIMITY_RADIUS = 100000
    var lat : Double = 39.1902658
    var lng : Double = -76.6100414
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    lateinit var storeViewIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get views and components from xml
        searchBar = findViewById(R.id.search_bar)
        searchButton = findViewById(R.id.containedButton)
        bottomTabs = findViewById(R.id.bottom_navigation)


        //fused initialized
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize the SDK
        val key = "AIzaSyBORW6PHdIIcO2wV8x_QSg8l_pW3NtYL9A"
        Places.initialize(applicationContext, key)

        //get list of stores nearby

        Log.d("storeList", "before size: " + storeList.size)
        var url = getUrl(lat, lng, "store")
        var dataTransfer = arrayOf<Object>(Object(), Object())
        dataTransfer[0] = storeList as Object
        dataTransfer[1] = url as Object

        Log.d("bruh", "Main url: " + dataTransfer[1].toString())

        var nbp : GetNearbyPlaces = GetNearbyPlaces()
        nbp.execute(*dataTransfer)
        Thread.sleep(1000)

        Log.d("storeList", "after size: " + storeList.size)
        //set up fragment for scrollable cards

        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)


        //get nearby places
        Log.d("storeList", "fragment size: " + MainActivity.storeList)
        val storeList = MainActivity.storeList

        val adapter = panel_adapter(storeList)
        recyclerView.adapter = adapter

        //autocomplete search bar

        searchBar.setOnClickListener{
            var fieldList :List<Place.Field> = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(this)
            startActivityForResult(intent, requestCode)
        }

        //Button On CLick Listener
        searchButton.setOnClickListener{
            this.startActivity(storeViewIntent)
        }


        //Tabs On Click Listener
        bottomTabs.setOnNavigationItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.page_1 -> {
                    searchBar.setText("")
                    searchBar.performClick()
                    false
                }
                R.id.page_2 -> {
                    false
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == requestCode && resultCode == RESULT_OK){
            var place: Place = Autocomplete.getPlaceFromIntent(data)
            searchBar.setText(place.name.toString())
            var lat = place.latLng.latitude.toString()
            var lng = place.latLng.longitude.toString()
            var coordinates = lat + "," + lng
            Log.d("coordinates", coordinates)
            var name: String? = place.name
            searchBar.setText(place.address)
            storeViewIntent = Intent(this.applicationContext, StoreViewActivity::class.java)
            storeViewIntent.putExtra("STORE_NAME", name)
            storeViewIntent.putExtra("STORE_COORDINATES", coordinates)
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), request_code)
        }

        var locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.setInterval(60000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setFastestInterval(5000)
        var locationCallback : LocationCallback = object : LocationCallback() {
            override fun onLocationResult(result : LocationResult){
                Toast.makeText(applicationContext, "result is : " + result, Toast.LENGTH_LONG).show()

                if(result == null){
                    Toast.makeText(applicationContext, "Current location is null ", Toast.LENGTH_LONG).show()
                }else{
                    for(location in result.locations){

                        if(location != null){
                            Toast.makeText(applicationContext, "current location is : " + location.latitude + "," + location.longitude, Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)

        var task : Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener (object : OnSuccessListener<Location>{
            override fun onSuccess(location: Location?) {
                if(location != null){
                    lat = location.latitude
                    lng = location.longitude

                    var latLng : LatLng = LatLng(lat, lng)
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == request_code) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
        }

    }

    private fun getUrl(latitude: Double, longitude : Double, nearbyPlace : String): String{
        var googlePlaceUrl : StringBuilder = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlaceUrl.append("location=" + latitude + "," + longitude)
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS)
        googlePlaceUrl.append("&type=" + nearbyPlace)
        googlePlaceUrl.append("&sensor=true")
        googlePlaceUrl.append("&key=" + "AIzaSyBORW6PHdIIcO2wV8x_QSg8l_pW3NtYL9A")

        return googlePlaceUrl.toString()
    }

    companion object{
        private const val label = "placeholder label"
        private const val requestCode = 100
        var storeList : ArrayList<StoreEntry> = ArrayList<StoreEntry>()
    }
}