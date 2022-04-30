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
        var product : StoreEntry = StoreEntry()
        product.storeName = "title"
        product.storeRating = "rating"
        storeList.add(product)

        val adapter = panel_adapter(storeList)
        recyclerView.adapter = adapter

        return view
    }

}