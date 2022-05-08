package com.example.rankmystore
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.io.IOException
import java.io.InputStream

class panel_adapter(private val storeList: List<StoreEntry>): RecyclerView.Adapter<StoreCardHolder>(){
    private lateinit var mContext: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreCardHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.store_panel, parent, false)
        mContext = parent.context
        return StoreCardHolder(layoutView)
    }

    override fun onBindViewHolder(holder: StoreCardHolder, position: Int) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if(position < storeList.size){
            val store = storeList[position]
            holder.storeTitle.text = store.storeName
            holder.productDesc.text = "Rating : "
            holder.latLng = store.lat.toString() + "," + store.lng.toString()

            //set image
            var loadImage: LoadImage = LoadImage()
            var urlLink = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png"
            var urlLink2 = "https://purepng.com/public/uploads/large/big-chungus-jkg.png"
            var bitmap : Bitmap = loadImage.execute(store.imageUrl).get()
            Log.d("store.imageUrl: ", store.imageUrl)
            holder.image.setImageBitmap(bitmap)
        }

        //comment
        holder.itemView.setOnClickListener{
            val storeViewIntent: Intent = Intent(mContext, StoreViewActivity::class.java)
            storeViewIntent.putExtra("STORE_NAME", holder.storeTitle.text)
            storeViewIntent.putExtra("STORE_COORDINATES", holder.latLng)
            mContext.startActivity(storeViewIntent)

        }

    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    class LoadImage : AsyncTask<String, Void, Bitmap>(){

        override fun doInBackground(vararg v: String?): Bitmap {
            var bitmap : Bitmap? = null
            var urlLink = v[0]
            Log.d("asdf", "urlLink: " + urlLink)

            var inputStream: InputStream = java.net.URL(urlLink).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            Log.d("bitmap" ,bitmap.toString())


            return bitmap!!
        }
    }

}