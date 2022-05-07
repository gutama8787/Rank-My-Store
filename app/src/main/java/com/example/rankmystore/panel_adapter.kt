package com.example.rankmystore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class panel_adapter(private val storeList: List<StoreEntry>): RecyclerView.Adapter<StoreCardHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreCardHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.store_panel, parent, false)

        return StoreCardHolder(layoutView)
    }

    override fun onBindViewHolder(holder: StoreCardHolder, position: Int) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if(position < storeList.size){
            val store = storeList[position]
            holder.storeTitle.text = store.storeName
            holder.productDesc.text = store.lat.toString() + "," + store.lng.toString()

        }

        //
        holder.itemView.setOnClickListener{
            holder.storeTitle.text = "clicked on this card"

        }

    }

    override fun getItemCount(): Int {
        return storeList.size
    }
}