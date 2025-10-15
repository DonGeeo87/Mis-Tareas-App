package dev.dongeeo.tareasdiarias.ui.screens

import android.app.Application
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import dev.dongeeo.tareasdiarias.AppGraph
import dev.dongeeo.tareasdiarias.domain.Task
import dev.dongeeo.tareasdiarias.ui.components.TaskItem
import dev.dongeeo.tareasdiarias.ui.components.EmptyStateIllustration
import dev.dongeeo.tareasdiarias.ui.viewmodel.TaskViewModel
import dev.dongeeo.tareasdiarias.ui.screens.EditTaskActivity
import dev.dongeeo.tareasdiarias.notifications.ReminderSchedulerImpl
import dev.dongeeo.tareasdiarias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    application: Application,
    onAddTask: () -> Unit,
    onOpenTask: (Task) -> Unit
) {
    val vm: TaskViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = AppGraph.provideRepository(application)
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repo, androidx.lifecycle.SavedStateHandle()) as T
        }
    })
    val state by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val editLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra(EditTaskActivity.EXTRA_TITLE)?.trim().orEmpty()
            val desc = data?.getStringExtra(EditTaskActivity.EXTRA_DESC)?.trim().orEmpty()
            val reminderAt = data?.getLongExtra(EditTaskActivity.EXTRA_REMINDER_AT, -1L)?.takeIf { it > 0 }
            val editing = pendingEditTask
            if (editing != null && title.isNotEmpty()) {
                vm.update(
                    editing.copy(
                        title = title,
                        description = desc,
                        reminderAtMillis = reminderAt,
                        updatedAtMillis = System.currentTimeMillis()
                    )
                )
                pendingEditTask = null
            } else {
                pendingEditTask = null
            }
        } else {
            pendingEditTask = null
        }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Text(
                        text = "Tareas de la Empresa",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.background(
                    brush = CorporateGradient
                )
            ) 
        },
        floatingActionButton = {
            var isFabPressed by remember { mutableStateOf(false) }
            val fabRotation by animateFloatAsState(
                targetValue = if (isFabPressed) 45f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "fabRotation"
            )
            
            FloatingActionButton(
                onClick = { 
                    isFabPressed = true
                    onAddTask()
                },
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 16.dp
                ),
                modifier = Modifier.background(
                    brush = CorporateGradient,
                    shape = MaterialTheme.shapes.large
                )
            ) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = "Agregar tarea",
                    modifier = Modifier.rotate(fabRotation)
                )
            }
        }
    ) { padding ->
        HomeContent(
            padding = padding,
            state = state,
            onOpenTask = onOpenTask,
            onEditTask = { task ->
                pendingEditTask = task
                val intent = Intent(context, EditTaskActivity::class.java)
                    .putExtra(EditTaskActivity.EXTRA_TITLE, task.title)
                    .putExtra(EditTaskActivity.EXTRA_DESC, task.description)
                editLauncher.launch(intent)
            },
            onDeleteTask = { task -> vm.delete(task) },
            onToggleReminder = { task ->
                if (task.reminderAtMillis != null) {
                    ReminderSchedulerImpl(context).cancel(task.id)
                    vm.update(task.copy(reminderAtMillis = null, updatedAtMillis = System.currentTimeMillis()))
                } else {
                    // Reusar editor para configurar recordatorio
                    pendingEditTask = task
                    val intent = Intent(context, EditTaskActivity::class.java)
                        .putExtra(EditTaskActivity.EXTRA_TITLE, task.title)
                        .putExtra(EditTaskActivity.EXTRA_DESC, task.description)
                    editLauncher.launch(intent)
                }
            },
            onToggleDone = { task ->
                vm.update(task.copy(isDone = !task.isDone, updatedAtMillis = System.currentTimeMillis()))
            }
        )
    }
}

@Composable
private fun HomeContent(
    padding: PaddingValues,
    state: dev.dongeeo.tareasdiarias.ui.viewmodel.HomeUiState,
    onOpenTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onToggleReminder: (Task) -> Unit,
    onToggleDone: (Task) -> Unit
) {
    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        state.errorMessage != null -> {
            Box(Modifier.fillMaxSize().padding(padding)) {
                Text(
                    text = state.errorMessage ?: "Error",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        state.tasks.isEmpty() -> {
            Box(Modifier.fillMaxSize().padding(padding)) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyStateIllustration(
                        isDarkTheme = MaterialTheme.colorScheme.background == MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = "Sin tareas",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Toca el botón + para agregar tu primera tarea",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(state.tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onClick = onOpenTask,
                        onQuickComplete = onToggleDone,
                        onQuickEdit = onEditTask,
                        onQuickDelete = onDeleteTask,
                        onQuickReminderToggle = onToggleReminder
                    )
                }
            }
        }
    }
}

private var pendingEditTask: Task? = null


