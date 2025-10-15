# TareasDiarias - Requirements

## Objetivo Principal
Desarrollar un prototipo Android para gestionar tareas con recordatorios y notificaciones, controlando correctamente el ciclo de vida de Activities y Fragments para evitar fugas de memoria o pérdida de datos.

## Funcionalidades Core
- [x] **Agregar tareas** con título, descripción y fecha/hora de recordatorio
- [x] **Editar/actualizar tareas** existentes con formulario intuitivo
- [x] **Listar tareas** con orden reciente y filtros visuales
- [x] **Programar recordatorios** y mostrar notificaciones automáticas
- [x] **Feedback visual/logs** de ciclo de vida (Activity/Fragment) con Toasts
- [x] **Marcar tareas como completadas** con animaciones
- [x] **Eliminar tareas** con confirmación visual

## Casos de Uso
### Trabajador
- ✅ Como trabajador, quiero crear una tarea con recordatorio para no olvidar pendientes
- ✅ Como trabajador, quiero ver mis tareas en una lista para priorizar
- ✅ Como trabajador, quiero editar una tarea para ajustar título/fecha/hora
- ✅ Como trabajador, quiero marcar tareas como completadas para seguimiento
- ✅ Como trabajador, quiero eliminar tareas que ya no necesito
- ✅ Como trabajador, quiero recibir notificaciones en el momento programado

## Restricciones Técnicas
- **Plataforma:** Android (minSdk 24, target/compile 36)
- **Tecnologías:** Kotlin, Jetpack Compose (UI moderna), Room, AlarmManager + BroadcastReceiver, NotificationCompat, Fragments dinámicos, RecyclerView (interop)
- **Estado:** ViewModel + SavedStateHandle; observar Room por Flow para sobrevivir a rotación
- **Permisos:** POST_NOTIFICATIONS (Android 13+) solo al programar recordatorios
- **UI/UX:** Gradientes corporativos (#009fe3, #312783), animaciones suaves, ilustraciones personalizadas

## Criterios de Aceptación
- [x] **CRUD funcional y persistente** en Room con operaciones completas
- [x] **Rotación no pierde datos** ni estado de selección (SavedStateHandle)
- [x] **Logs/Toasts en callbacks** de ciclo de vida (MainActivity + TaskDetailFragment)
- [x] **Notificación cuando el recordatorio** se dispara (AlarmManager + BroadcastReceiver)
- [x] **Permiso solicitado solo cuando corresponde** (runtime en Android 13+)
- [x] **Fragment recibe parámetros** vía factory (`newInstance`) y `Bundle`
- [x] **RecyclerView con adapter personalizado** y DiffUtil para optimización
- [x] **startActivityForResult** implementado para edición de tareas
- [x] **UI moderna** con gradientes, animaciones y feedback visual

## Métricas de Éxito ✅
- ✅ **Tiempo para crear tarea < 15s** - Formulario intuitivo con validación
- ✅ **Tasa de notificaciones mostradas > 95%** - AlarmManager + setExactAndAllowWhileIdle
- ✅ **UI responsiva** - Animaciones suaves < 300ms
- ✅ **Feedback visual inmediato** - Toasts y animaciones en todas las interacciones

## Riesgos y Suposiciones (opcional)
- Riesgo: Doze puede diferir `setExactAndAllowWhileIdle`
- Suposición: No hay sincronización remota; datos locales son suficientes

## Referencias Externas
#[[file:../TareasDiarias/design.md]]
#[[file:../TareasDiarias/tasks.md]]

---
Desarrollador: Giorgio Interdonato Palacios — GitHub @DonGeeo87

