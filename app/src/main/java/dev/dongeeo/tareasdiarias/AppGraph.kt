package dev.dongeeo.tareasdiarias

import android.app.Application
import dev.dongeeo.tareasdiarias.data.AppDatabase
import dev.dongeeo.tareasdiarias.data.TaskRepository

/**
 * Proveedor simple de dependencias para mantener el ejemplo sin DI framework.
 */
object AppGraph {
    @Volatile private var repositoryInstance: TaskRepository? = null

    fun provideRepository(app: Application): TaskRepository {
        val existing = repositoryInstance
        if (existing != null) return existing
        val db = AppDatabase.getInstance(app)
        val repo = TaskRepository(db.taskDao())
        repositoryInstance = repo
        return repo
    }
}


