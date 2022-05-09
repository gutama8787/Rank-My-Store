package com.example.rankmystore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoreCardHolder(storeView : View) : RecyclerView.ViewHolder(storeView), View.OnClickListener{
    var storeTitle: TextView = storeView.findViewById(R.id.cardTitle)
    var productDesc: TextView = storeView.findViewById(R.id.cardDescription)
    var rating: TextView = storeView.findViewById(R.id.rating)
    var image: ImageView = storeView.findViewById(R.id.cardImage)
    var latLng : String = ""

    // comment
    // rating
    //
    override fun onClick(v: View) {
        storeTitle.setText("clicked on this card!")
    }
}