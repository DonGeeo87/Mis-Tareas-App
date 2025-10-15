# TareasDiarias - Design

## Arquitectura General
### Patrón Arquitectónico
Clean Architecture + MVVM. UI en Jetpack Compose; Fragment para demostración de ciclo de vida y paso de parámetros.

### Capas del Sistema
- **Presentation:** Compose screens, `TaskViewModel`, `EditTaskActivity`, `FragmentHostActivity`, `TaskDetailFragment`.
- **Domain:** Modelo `Task` y casos de uso (CRUD y agendado de recordatorios).
- **Data:** Room (`TaskEntity`, `TaskDao`, `AppDatabase`, `TaskRepository`).

## Modelos de Datos
```kotlin
data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val reminderAtMillis: Long?,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)
```

## Flujo de Navegación
HomeScreen -> EditTaskActivity (crear/editar)
HomeScreen -> FragmentHostActivity -> TaskDetailFragment(taskId)

## Componentes Principales
**TaskViewModel**
- Responsabilidad: Orquestar flujos de tareas (Flow de Room), exponer UI state, manejar creación/edición/borrado.
- Dependencias: Use cases.

**EditTaskActivity**
- Responsabilidad: Formulario Compose para crear/editar; devuelve resultado vía Activity Result.

**TaskDetailFragment**
- Responsabilidad: Mostrar detalle con información completa; logs de ciclo de vida; recibe `taskId` por `Bundle`.

## Decisiones Técnicas
### Base de Datos
- Tipo: Room; tabla `tasks` con índices implícitos por PK.

### Networking
- No aplica (prototipo local).

### UI Framework
- **Jetpack Compose (Material 3)** con interop RecyclerView mediante `AndroidView`
- **Gradientes corporativos** (#009fe3 → #312783) en TopAppBars, FABs y bordes
- **Animaciones suaves** con `animateFloatAsState`, `spring` y `tween`
- **Ilustraciones personalizadas** con Canvas/Path para estado vacío
- **Jerarquía visual mejorada** con tipografía, colores y elevaciones
- **Feedback visual contextual** con Toasts informativos y emojis para todas las acciones del usuario
- **Validación en tiempo real** con mensajes de error claros y útiles

### Notificaciones
- **AlarmManager.setExactAndAllowWhileIdle** + **BroadcastReceiver** para disparo
- **NotificationCompat** con canal `TASKS_CHANNEL` y iconos personalizados
- **ReminderSchedulerImpl** para gestión centralizada de recordatorios
- **NotificationHelper** con configuración de canal automática

### Permisos
- **POST_NOTIFICATIONS** solicitado en runtime al programar primer recordatorio en Android 13+
- **ActivityResultContracts.RequestPermission** para manejo moderno de permisos
- **Pending task queue** para programar recordatorio tras otorgar permiso

---
Desarrollador: Giorgio Interdonato Palacios — GitHub @DonGeeo87

