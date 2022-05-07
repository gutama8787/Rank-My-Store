package com.example.rankmystore

import android.R.attr.apiKey
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    lateinit var searchBar: TextInputEditText
    lateinit var searchButton : Button
    lateinit var bottomTabs : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //comment

        // Initialize the SDK

        val key = "AIzaSyCMEYURMvccFilVqreWH0j3Mi64cM2Zj5Y"
        Places.initialize(applicationContext, key)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        val fragment = StoreGridFragment()
        val fragmentManager = supportFragmentManager

        if(null == fragmentManager.findFragmentById(R.id.fragment_container)) {
            val fragmentTransaction1 = fragmentManager.beginTransaction()
            fragmentTransaction1.add(R.id.fragment_container, fragment)
            fragmentTransaction1.commit()
        }

        //get views and components from xml
        searchBar = findViewById(R.id.search_bar)
        searchButton = findViewById(R.id.containedButton)
        bottomTabs = findViewById(R.id.bottom_navigation)

        //Button On CLick Listener
        searchButton.setOnClickListener{
            var searchText = searchBar.text.toString()


        }

        //Tabs On Click Listener
        bottomTabs.setOnNavigationItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.page_1 -> {
                    searchBar.setText("")
                    true
                }
                R.id.page_2 -> {
                    false
                }
                R.id.page_3 -> {
                    false
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
            searchBar.setText(place.latLng.toString())

            //searchBar.setText(place.address)
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
        googlePlaceUrl.append("&key=" + "AIzaSyAokj6tSKAL653T9lBjnaRfO2xiW4WjLgs")

        return googlePlaceUrl.toString()
    }
    companion object{
        private const val label = "placeholder label"
    }
}