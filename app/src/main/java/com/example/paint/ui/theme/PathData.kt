package com.example.paint.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

//это data класс для сохранения цвета и толщины линий
data class PathData(
    val path: Path = Path(),
    val color: Color = Color.Blue //по умолчанию синий

)
