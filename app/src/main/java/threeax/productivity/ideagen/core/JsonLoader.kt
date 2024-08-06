package threeax.productivity.ideagen.core

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

data class QOTDData(val text: String, val from: String) // Replace with your data class
data class MOTDData(val text: String) // Replace with your data class

class JsonLoader(private val context: Context) {
    fun loadQOTDJsonData(file: String): List<QOTDData> {
        val assetManager: AssetManager = context.assets
        val inputStream = assetManager.open(file)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        inputStream.close()

        val jsonString = stringBuilder.toString()
        val gson = Gson()
        val type = object : TypeToken<List<QOTDData>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun loadMOTDJsonData(file: String): List<MOTDData> {
        val assetManager: AssetManager = context.assets
        val inputStream = assetManager.open(file)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        inputStream.close()

        val jsonString = stringBuilder.toString()
        val gson = Gson()
        val type = object : TypeToken<List<MOTDData>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}
