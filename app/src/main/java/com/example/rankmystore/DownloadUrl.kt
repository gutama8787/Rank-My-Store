package com.example.rankmystore

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DownloadUrl {
    fun readUrl(myUrl: String): String{
        var data : String = ""
        var inputStream : InputStream? = null
        var urlConnection : HttpURLConnection? = null

        Log.d("bruh", "myUrl: " + myUrl)
        try{
            var url : URL = URL(myUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()

            inputStream = urlConnection.inputStream
            var br : BufferedReader = BufferedReader(InputStreamReader(inputStream))
            var sb : StringBuffer = StringBuffer()

            var line: String? = br.readLine()

            while(line != null){
                sb.append(line)
                line = br.readLine()
            }

            data = sb.toString()
        }catch (e: MalformedURLException){
            e.printStackTrace()
        } catch (e : IOException) {
            e.printStackTrace();
        }
        finally{

            inputStream?.close()
            urlConnection?.disconnect()
        }


        Log.d("bruh", "data from DownloadUrl: " + data)
        return data
    }
}