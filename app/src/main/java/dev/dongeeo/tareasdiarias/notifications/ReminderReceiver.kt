package dev.dongeeo.tareasdiarias.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Recordatorio"
        val desc = intent.getStringExtra(EXTRA_DESC) ?: "Tarea pendiente"
        val id = intent.getIntExtra(EXTRA_ID, 0)
        NotificationHelper.notify(context, id, title, desc)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
    }
}


