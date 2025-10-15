package dev.dongeeo.tareasdiarias.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.dongeeo.tareasdiarias.R

object NotificationHelper {
    const val CHANNEL_ID = "TASKS_CHANNEL"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios de tareas",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    fun notify(context: Context, notificationId: Int, title: String, content: String) {
        ensureChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}


