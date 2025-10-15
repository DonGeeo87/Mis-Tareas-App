package dev.dongeeo.tareasdiarias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.dongeeo.tareasdiarias.ui.theme.TareasDiariasTheme
import dev.dongeeo.tareasdiarias.ui.screens.HomeScreen
import android.content.Intent
import dev.dongeeo.tareasdiarias.ui.screens.EditTaskActivity
import androidx.activity.result.ActivityResultLauncher
import dev.dongeeo.tareasdiarias.AppGraph
import dev.dongeeo.tareasdiarias.domain.Task
import dev.dongeeo.tareasdiarias.domain.usecase.AddTaskUseCase
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dev.dongeeo.tareasdiarias.domain.usecase.ScheduleReminderUseCase
import dev.dongeeo.tareasdiarias.notifications.ReminderSchedulerImpl
import dev.dongeeo.tareasdiarias.ui.screens.FragmentHostActivity

class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"
    private var pendingTaskForSchedule: Task? = null
    private lateinit var requestNotifPermission: androidx.activity.result.ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val scheduler = ScheduleReminderUseCase(ReminderSchedulerImpl(this))
        requestNotifPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                pendingTaskForSchedule?.let { scheduler(it) }
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
            pendingTaskForSchedule = null
        }
        val editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val title = data?.getStringExtra(EditTaskActivity.EXTRA_TITLE)?.trim().orEmpty()
                val desc = data?.getStringExtra(EditTaskActivity.EXTRA_DESC)?.trim().orEmpty()
                val reminderAt = data?.getLongExtra(EditTaskActivity.EXTRA_REMINDER_AT, -1L)?.takeIf { it > 0 }
                if (title.isNotEmpty()) {
                    val now = System.currentTimeMillis()
                    val repo = AppGraph.provideRepository(application)
                    val add = AddTaskUseCase(repo)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val id = add(
                            Task(
                                title = title,
                                description = desc,
                                reminderAtMillis = reminderAt,
                                createdAtMillis = now,
                                updatedAtMillis = now
                            )
                        )
                        if (reminderAt != null) {
                            val fullTask = Task(
                                id = id,
                                title = title,
                                description = desc,
                                reminderAtMillis = reminderAt,
                                createdAtMillis = now,
                                updatedAtMillis = now
                            )
                            runOnUiThread {
                                if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                                ) {
                                    pendingTaskForSchedule = fullTask
                                    requestNotifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    scheduler(fullTask)
                                }
                            }
                        }
                    }
                }
            }
        }
        setContent {
            TareasDiariasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        application = application,
                        onAddTask = {
                            val intent = Intent(this, EditTaskActivity::class.java)
                            editLauncher.launch(intent)
                        },
                        onOpenTask = { task ->
                            val intent = Intent(this, FragmentHostActivity::class.java)
                                .putExtra(FragmentHostActivity.EXTRA_TASK_ID, task.id)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart")
        Toast.makeText(this, "Activity onStart", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    TareasDiariasTheme {
        Text("Preview")
    }
}