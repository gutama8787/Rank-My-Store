package com.example.rankmystore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val storeList: List<StoreEntry>): RecyclerView.Adapter<ReviewCardHolder>(){
    private lateinit var mContext: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewCardHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.review_panel, parent, false)
        mContext = parent.context
        return ReviewCardHolder(layoutView)
    }

    override fun onBindViewHolder(holder: ReviewCardHolder, position: Int) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if(position < storeList.size){
            val store = storeList[position]
            holder.storeTitle.text = store.storeName
            holder.productDesc.text = store.comment
            holder.rating.text = store.storeRating
            holder.image.setImageBitmap(store.img)
        }
    }

    override fun getItemCount(): Int {
        return storeList.size
    }
}