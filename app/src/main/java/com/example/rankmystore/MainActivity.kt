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

    companion object{
        private const val label = "placeholder label"
    }
}