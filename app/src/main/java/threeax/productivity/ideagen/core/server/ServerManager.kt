package threeax.productivity.ideagen.core.server

import android.content.Context
import androidx.compose.animation.core.infiniteRepeatable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ListenableWorker
import androidx.work.Logger
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.json
import threeax.productivity.ideagen.core.AppData
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.persistence.ActivityDBHandler
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL



data class UploadActivityResponse(
    val code: String,
    val res: ArrayList<String>
)

class UploadActivityResponseParser<InputStream> {
    fun parse(stream: InputStream?): UploadActivityResponse {
        val jsonResponse = Gson().fromJson(stream.toString(), UploadActivityResponse::class.java)
        return jsonResponse
    }
}


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class UploadActivity(
    private val responseParser: UploadActivityResponseParser<Any?>,
    settings: Settings
) {
    private val requestUrl = "http://" + settings.getString("server", "serverUrl").toString() + ":" + settings.getString("server", "serverPort").toString() + "/post/activity"
    private val isEnable = settings.getString("server", "enableServer").toString().toBoolean()

    // Function that makes the network request, blocking the current thread
    fun makeRequest(
        context: Context
    ): Result<UploadActivityResponse> {
        if (isEnable) return Result.Error(Exception("Server is disabled"))
        System.out.println("Making request")

        val db = ActivityDBHandler(context)

        val jsonString = Gson().toJson(db.readPublicActivities())
        val url = URI(requestUrl)

        (url.toURL().openConnection() as? HttpURLConnection)?.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            doOutput = true
            outputStream.write(jsonString.toByteArray())
            return Result.Success(responseParser.parse(inputStream))
        }
        return Result.Error(Exception("Cannot open HttpURLConnection"))
    }
}

class UploadActivityViewModel(
    private val uploadActivity: UploadActivity
): ViewModel() {

    fun syncActivities(context: Context) {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {
            uploadActivity.makeRequest(context)
        }
    }
}