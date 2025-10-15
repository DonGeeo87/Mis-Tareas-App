package dev.dongeeo.tareasdiarias.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.dongeeo.tareasdiarias.domain.Task
import dev.dongeeo.tareasdiarias.domain.usecase.ReminderScheduler

class ReminderSchedulerImpl(private val context: Context) : ReminderScheduler {
    override fun schedule(task: Task) {
        val whenMillis = task.reminderAtMillis ?: return
        if (task.id <= 0L) {
            android.util.Log.w("ReminderScheduler", "No se programa: id invalido")
            return
        }
        if (whenMillis <= System.currentTimeMillis()) {
            android.util.Log.w("ReminderScheduler", "No se programa: fecha pasada")
            return
        }
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_ID, task.id.toInt())
            putExtra(ReminderReceiver.EXTRA_TITLE, task.title)
            putExtra(ReminderReceiver.EXTRA_DESC, task.description)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, task.id.toInt(), intent, flags)
        val am = context.getSystemService(AlarmManager::class.java)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, pi)
    }

    override fun cancel(taskId: Long) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, taskId.toInt(), intent, flags)
        val am = context.getSystemService(AlarmManager::class.java)
        am.cancel(pi)
    }
}


