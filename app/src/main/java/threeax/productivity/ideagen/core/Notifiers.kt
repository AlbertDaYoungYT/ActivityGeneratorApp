package threeax.productivity.ideagen.core

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat

class Notifiers(
    nManager: NotificationManager,
    context: Context,
    val settings: Settings = Settings(context)
) {

    private var canDisplayNotifications: Boolean = false

    private val notificationManager: NotificationManager = nManager
    private val mainContext: Context = context

    fun checkNotificationPermission(): Boolean {
        with(NotificationManagerCompat.from(mainContext)) {
            if (ActivityCompat.checkSelfPermission(
                    mainContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                canDisplayNotifications = false
            }
            canDisplayNotifications = true
        }

        return canDisplayNotifications
    }

    fun createNotificationChannel(channelId: String, name: String, descriptionText: String) {
        if (settings.getSetting("notifications") == "true") {
            checkNotificationPermission()
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is not in the Support Library.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                this.notificationManager.createNotificationChannel(channel)
            }
        }
    }
}