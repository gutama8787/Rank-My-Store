package com.example.rankmystore

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DataParser {
    fun getPlace(googlePlacesJson : JSONObject) : HashMap<String, String>{
        var googlePlacesMap : HashMap<String, String> = HashMap()
        var placeName : String = "NA"
        var vicinity : String = "NA"
        var latitude: String = ""
        var longitude: String = ""
        var reference : String = ""
        var url: String = ""

        if(!googlePlacesJson.isNull("name")){
            placeName = googlePlacesJson.getString("name")
            //Log.d("sadf", "name is : " + placeName)
        }
        if(!googlePlacesJson.isNull("vicinity")){
            vicinity = googlePlacesJson.getString("vicinity")
        }

        latitude = googlePlacesJson.getJSONObject("geometry").getJSONObject("location").getString("lat")
        longitude = googlePlacesJson.getJSONObject("geometry").getJSONObject("location").getString("lng")

        reference = googlePlacesJson.getString("reference")
        url = googlePlacesJson.getString("icon")
        Log.d("hashmap url put", url)

        googlePlacesMap.put("place_name", placeName)
        googlePlacesMap.put("vicinity", vicinity)
        googlePlacesMap.put("lat", latitude)
        googlePlacesMap.put("lng", longitude)
        googlePlacesMap.put("reference", reference)
        googlePlacesMap.put("url", url)



        return googlePlacesMap
    }

    fun getPlaces(jsonArray: JSONArray): List<HashMap<String, String>>{
        var count = jsonArray.length()
        var placesList : ArrayList<HashMap<String, String>> = ArrayList()
        var placeMap : HashMap<String, String>? = null

        for(i in 0 until count){
            try {
                placeMap = getPlace(jsonArray.get(i) as JSONObject)
                placesList.add(placeMap)
            }catch (e : JSONException){
                e.printStackTrace()
            }

        }

        return placesList
    }

    fun parse(jsonData : String): List<HashMap<String, String>>{
        var jsonObject : JSONObject? = null
        lateinit var jsonArray: JSONArray

        Log.d("bruh", "parse() url: " + jsonData)

        jsonObject = JSONObject(jsonData)
        jsonArray = jsonObject.getJSONArray("results")

        return getPlaces(jsonArray)
    }

}