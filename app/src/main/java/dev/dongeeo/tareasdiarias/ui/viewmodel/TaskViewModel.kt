package dev.dongeeo.tareasdiarias.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dongeeo.tareasdiarias.data.TaskRepository
import dev.dongeeo.tareasdiarias.domain.Task
import dev.dongeeo.tareasdiarias.domain.usecase.AddTaskUseCase
import dev.dongeeo.tareasdiarias.domain.usecase.DeleteTaskUseCase
import dev.dongeeo.tareasdiarias.domain.usecase.GetTasksUseCase
import dev.dongeeo.tareasdiarias.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TaskViewModel(
    private val repository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val getTasks = GetTasksUseCase(repository)
    private val addTask = AddTaskUseCase(repository)
    private val updateTask = UpdateTaskUseCase(repository)
    private val deleteTask = DeleteTaskUseCase(repository)

    val uiState: StateFlow<HomeUiState> = getTasks().map { list ->
        HomeUiState(tasks = list)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun add(title: String, description: String, reminderAtMillis: Long?) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            addTask(
                Task(
                    title = title,
                    description = description,
                    reminderAtMillis = reminderAtMillis,
                    createdAtMillis = now,
                    updatedAtMillis = now
                )
            )
        }
    }

    fun update(task: Task) {
        viewModelScope.launch { updateTask(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch { deleteTask(task) }
    }
}


