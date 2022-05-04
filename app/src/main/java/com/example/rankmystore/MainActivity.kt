package com.example.rankmystore

import android.R.attr.apiKey
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get views and components from xml
        searchBar = findViewById(R.id.search_bar)
        searchButton = findViewById(R.id.containedButton)
        bottomTabs = findViewById(R.id.bottom_navigation)

        // Initialize the SDK
        val key = "AIzaSyAHfN_f1maB_qp5FM0Q69kIDl4ne1rNiNM"
        Places.initialize(applicationContext, key)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        //set up fragment for scrollable cards
        val fragment = StoreGridFragment()
        val fragmentManager = supportFragmentManager

        if(null == fragmentManager.findFragmentById(R.id.fragment_container)) {
            val fragmentTransaction1 = fragmentManager.beginTransaction()
            fragmentTransaction1.add(R.id.fragment_container, fragment)
            fragmentTransaction1.commit()
        }

        //autocomplete search bar

        searchBar.setOnClickListener{
            var fieldList :List<Place.Field> = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(this)
            startActivityForResult(intent, requestCode)
        }

        //Button On CLick Listener
        searchButton.setOnClickListener{
            var searchText = searchBar.text.toString()


        }


        //Tabs On Click Listener
        bottomTabs.setOnNavigationItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.page_1 -> {
                    //searchBar.setText("")
                    //searchBar.requestFocus()
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
            searchBar.setText(place.address)
        }
    }

    companion object{
        private const val label = "placeholder label"
        private const val requestCode = 100
    }
}