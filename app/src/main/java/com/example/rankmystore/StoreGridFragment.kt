package com.example.rankmystore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreGridFragment: Fragment(){
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
        recyclerView.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)

        val storeList = mutableListOf<StoreEntry>()
        var product1 : StoreEntry = StoreEntry()
        product1.storeName = "title1"
        product1.storeRating = "rating"
        var product2 : StoreEntry = StoreEntry()
        product2.storeName = "title 2"
        product2.storeRating = "rating 2"
        var product3 : StoreEntry = StoreEntry()
        product3.storeName = "title3"
        product3.storeRating = "rating"
        var product4 : StoreEntry = StoreEntry()
        product4.storeName = "BRUH"
        product4.storeRating = "rating 2"
        storeList.add(product1)
        storeList.add(product2)
        storeList.add(product3)
        storeList.add(product4)

        val adapter = panel_adapter(storeList)
        recyclerView.adapter = adapter

        return view
    }

}