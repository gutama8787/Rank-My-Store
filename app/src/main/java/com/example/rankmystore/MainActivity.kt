package com.example.rankmystore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //comment

        val fragment = StoreGridFragment()
        val fragmentManager = supportFragmentManager

        if(null == fragmentManager.findFragmentById(R.id.fragment_container)) {
            val fragmentTransaction1 = fragmentManager.beginTransaction()
            fragmentTransaction1.add(R.id.fragment_container, fragment)
            fragmentTransaction1.commit()
        }
    }

    companion object{
        private const val label = "placeholder label"
    }
}