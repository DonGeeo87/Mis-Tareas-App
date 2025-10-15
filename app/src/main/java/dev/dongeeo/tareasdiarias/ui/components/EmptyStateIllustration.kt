package dev.dongeeo.tareasdiarias.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import dev.dongeeo.tareasdiarias.ui.theme.CorporateBlue
import dev.dongeeo.tareasdiarias.ui.theme.CorporatePurple
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun EmptyStateIllustration(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false
) {
    val primaryColor = if (isDarkTheme) CorporateBlue else CorporateBlue
    val secondaryColor = if (isDarkTheme) CorporatePurple else CorporatePurple
    
    // Animación de flotación
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    // Animación de rotación sutil
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Canvas(
        modifier = modifier
            .size(120.dp)
            .alpha(0.8f)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2 + floatOffset
        
        // Dibujar elementos con rotación sutil
        rotate(rotationAngle * 0.1f, Offset(centerX, centerY)) {
            drawTaskElements(
                centerX = centerX,
                centerY = centerY,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor
            )
        }
    }
}

private fun DrawScope.drawTaskElements(
    centerX: Float,
    centerY: Float,
    primaryColor: Color,
    secondaryColor: Color
) {
    val strokeWidth = 4.dp.toPx()
    
    // Tarea principal (centro)
    val mainTaskSize = 40.dp.toPx()
    val mainTaskPath = Path().apply {
        addRoundRect(
            roundRect = androidx.compose.ui.geometry.RoundRect(
                left = centerX - mainTaskSize / 2,
                top = centerY - mainTaskSize / 2,
                right = centerX + mainTaskSize / 2,
                bottom = centerY + mainTaskSize / 2,
                radiusX = 8.dp.toPx(),
                radiusY = 8.dp.toPx()
            )
        )
    }
    
    drawPath(
        path = mainTaskPath,
        color = primaryColor,
        style = Stroke(width = strokeWidth)
    )
    
    // Checkmark en la tarea principal
    val checkPath = Path().apply {
        moveTo(centerX - 8.dp.toPx(), centerY)
        lineTo(centerX - 2.dp.toPx(), centerY + 6.dp.toPx())
        lineTo(centerX + 10.dp.toPx(), centerY - 8.dp.toPx())
    }
    drawPath(
        path = checkPath,
        color = primaryColor,
        style = Stroke(width = strokeWidth * 0.8f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
    )
    
    // Tareas secundarias (esquinas)
    val cornerTasks = listOf(
        Offset(centerX - 35.dp.toPx(), centerY - 25.dp.toPx()), // Superior izquierda
        Offset(centerX + 30.dp.toPx(), centerY - 20.dp.toPx()), // Superior derecha
        Offset(centerX - 30.dp.toPx(), centerY + 25.dp.toPx()), // Inferior izquierda
        Offset(centerX + 35.dp.toPx(), centerY + 20.dp.toPx())  // Inferior derecha
    )
    
    cornerTasks.forEachIndexed { index, offset ->
        val taskSize = 25.dp.toPx()
        val color = if (index % 2 == 0) secondaryColor else primaryColor.copy(alpha = 0.6f)
        
        val taskPath = Path().apply {
            addRoundRect(
                roundRect = androidx.compose.ui.geometry.RoundRect(
                    left = offset.x - taskSize / 2,
                    top = offset.y - taskSize / 2,
                    right = offset.x + taskSize / 2,
                    bottom = offset.y + taskSize / 2,
                    radiusX = 6.dp.toPx(),
                    radiusY = 6.dp.toPx()
                )
            )
        }
        
        drawPath(
            path = taskPath,
            color = color,
            style = Stroke(width = strokeWidth * 0.8f)
        )
        
        // Líneas de conexión sutiles
        val connectionPath = Path().apply {
            moveTo(offset.x, offset.y)
            lineTo(centerX, centerY)
        }
        drawPath(
            path = connectionPath,
            color = color.copy(alpha = 0.3f),
            style = Stroke(width = 1.dp.toPx())
        )
    }
    
    // Elementos decorativos (puntos)
    val dots = listOf(
        Offset(centerX - 45.dp.toPx(), centerY - 40.dp.toPx()),
        Offset(centerX + 40.dp.toPx(), centerY - 35.dp.toPx()),
        Offset(centerX - 40.dp.toPx(), centerY + 35.dp.toPx()),
        Offset(centerX + 45.dp.toPx(), centerY + 40.dp.toPx())
    )
    
    dots.forEach { dotOffset ->
        drawCircle(
            color = primaryColor.copy(alpha = 0.4f),
            radius = 3.dp.toPx(),
            center = dotOffset
        )
    }
}
