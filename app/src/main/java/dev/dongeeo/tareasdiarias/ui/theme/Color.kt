package dev.dongeeo.tareasdiarias.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Colores corporativos principales
val CorporateBlue = Color(0xFF009FE3)
val CorporatePurple = Color(0xFF312783)

// Tonos complementarios para gradientes y acentos
val CorporateBlueLight = Color(0xFF4DB8E8)
val CorporatePurpleLight = Color(0xFF5A4A9A)
val CorporateOrange = Color(0xFFFF6B35)
val CorporatePink = Color(0xFFFF4081)

// Variantes más suaves para bordes y acentos
val CorporateBlueAlpha = Color(0x1A009FE3) // 10% opacity
val CorporatePurpleAlpha = Color(0x1A312783) // 10% opacity
val CorporateBlueSemi = Color(0x66009FE3) // 40% opacity

// Gradientes corporativos
val CorporateGradient = Brush.horizontalGradient(listOf(CorporateBlue, CorporatePurple))
val CorporateGradientVertical = Brush.verticalGradient(listOf(CorporateBlue, CorporatePurple))
val CorporateGradientDiagonal = Brush.linearGradient(
    colors = listOf(CorporateBlue, CorporatePurple),
    start = androidx.compose.ui.geometry.Offset(0f, 0f),
    end = androidx.compose.ui.geometry.Offset(1f, 1f)
)
val AccentGradient = Brush.horizontalGradient(listOf(CorporateOrange, CorporatePink))

// Gradientes sutiles para fondos
val CorporateGradientSubtle = Brush.horizontalGradient(
    listOf(CorporateBlueAlpha, CorporatePurpleAlpha)
)
val CorporateGradientSemi = Brush.horizontalGradient(
    listOf(CorporateBlueSemi, CorporatePurpleAlpha)
)

// Colores para modo oscuro
val CorporateBlueDark = Color(0xFF0078B8)
val CorporatePurpleDark = Color(0xFF251F5A)

// Gradiente para modo oscuro
val CorporateGradientDark = Brush.horizontalGradient(listOf(CorporateBlueDark, CorporatePurpleDark))

// Colores de estado
val SuccessGreen = Color(0xFF4CAF50)
val WarningOrange = Color(0xFFFF9800)
val ErrorRed = Color(0xFFF44336)

// Helpers para aplicar gradientes en composables
fun androidx.compose.ui.graphics.Brush.toColor(): Color = CorporateBlue // Fallback para TopAppBar

// Sombras con tinte corporativo
val CorporateShadowColor = CorporateBlue.copy(alpha = 0.3f)
val CorporateShadowColorDark = CorporatePurple.copy(alpha = 0.4f)