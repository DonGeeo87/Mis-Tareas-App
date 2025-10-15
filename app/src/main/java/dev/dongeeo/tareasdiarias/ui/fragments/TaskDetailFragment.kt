package dev.dongeeo.tareasdiarias.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import dev.dongeeo.tareasdiarias.data.AppDatabase
import dev.dongeeo.tareasdiarias.domain.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.dongeeo.tareasdiarias.ui.theme.SuccessGreen
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class TaskDetailFragment : Fragment() {

    companion object {
        private const val ARG_TASK_ID = "arg_task_id"
        fun newInstance(taskId: Long): TaskDetailFragment = TaskDetailFragment().apply {
            arguments = bundleOf(ARG_TASK_ID to taskId)
        }
        private const val TAG = "TaskDetailFragment"
    }

    private val taskId: Long by lazy { requireArguments().getLong(ARG_TASK_ID) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        Toast.makeText(context, "Fragment onAttach", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate taskId=$taskId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        val compose = ComposeView(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getInstance(requireContext()).taskDao()
            val entity = runCatching { dao.getById(taskId) }.getOrNull()
            val domain = entity?.toDomain()
            compose.post {
                compose.setContent {
                    DetailScreen(
                        task = domain,
                        onBack = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                        onEdit = { current ->
                            // Lanzar editor de forma simple: abrir EditTaskActivity sin result (fuera de alcance aquí)
                            android.content.Intent(requireContext(), dev.dongeeo.tareasdiarias.ui.screens.EditTaskActivity::class.java).apply {
                                putExtra(dev.dongeeo.tareasdiarias.ui.screens.EditTaskActivity.EXTRA_TITLE, current.title)
                                putExtra(dev.dongeeo.tareasdiarias.ui.screens.EditTaskActivity.EXTRA_DESC, current.description)
                            }.also { startActivity(it) }
                        },
                        onDelete = { current ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                val dao = AppDatabase.getInstance(requireContext()).taskDao()
                                dao.delete(dev.dongeeo.tareasdiarias.data.TaskEntity(
                                    id = current.id,
                                    title = current.title,
                                    description = current.description,
                                    reminderAtMillis = current.reminderAtMillis,
                                    isDone = current.isDone,
                                    createdAtMillis = current.createdAtMillis,
                                    updatedAtMillis = current.updatedAtMillis
                                ))
                                requireActivity().runOnUiThread { requireActivity().finish() }
                            }
                        }
                    )
                }
            }
        }
        return compose
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

private fun dev.dongeeo.tareasdiarias.data.TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    reminderAtMillis = reminderAtMillis,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(task: Task?, onBack: () -> Unit, onEdit: (Task) -> Unit, onDelete: (Task) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = task?.title ?: "Detalle de tarea",
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = { 
                    IconButton(onClick = onBack) { 
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") 
                    } 
                },
                actions = {
                    if (task != null) {
                        IconButton(onClick = { onEdit(task) }) { 
                            Icon(Icons.Filled.Edit, contentDescription = "Editar") 
                        }
                        IconButton(onClick = { onDelete(task) }) { 
                            Icon(Icons.Filled.Delete, contentDescription = "Borrar") 
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (task == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tarea no encontrada", 
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            Card(
                modifier = Modifier.fillMaxWidth().padding(padding).padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (task.isDone) 
                        MaterialTheme.colorScheme.surfaceVariant 
                    else 
                        MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Título con estado completado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = task.title, 
                            style = MaterialTheme.typography.titleLarge,
                            textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                            color = if (task.isDone) 
                                MaterialTheme.colorScheme.onSurfaceVariant 
                            else 
                                MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Estado",
                            tint = if (task.isDone) SuccessGreen else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    // Descripción
                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description, 
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Badges de información
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (task.reminderAtMillis != null) {
                            AssistChip(
                                onClick = { /* TODO: mostrar detalles del recordatorio */ },
                                label = { 
                                    Text(
                                        text = df.format(Date(task.reminderAtMillis)),
                                        style = MaterialTheme.typography.labelSmall
                                    ) 
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Notifications, 
                                        contentDescription = "Recordatorio",
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                        
                        AssistChip(
                            onClick = { /* TODO: mostrar fecha de creación */ },
                            label = { 
                                Text(
                                    text = "Creada: ${df.format(Date(task.createdAtMillis))}",
                                    style = MaterialTheme.typography.labelSmall
                                ) 
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }
                }
            }
        }
    }
}


