package com.example.rankmystore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewCardHolder(storeView : View) : RecyclerView.ViewHolder(storeView), View.OnClickListener{
    //var storeImage: ImageView = storeView.findViewById(androidx.appcompat.R.id.icon)
    var storeTitle: TextView = storeView.findViewById(R.id.cardTitle_)
    var productDesc: TextView = storeView.findViewById(R.id.cardDescription_)
    var rating: TextView = storeView.findViewById(R.id.rating_)
    var image: ImageView = storeView.findViewById(R.id.cardImage_)
    var latLng : String = ""

    // comment
    // rating
    //
    override fun onClick(v: View) {
        storeTitle.setText("clicked on this card!")
    }
}
