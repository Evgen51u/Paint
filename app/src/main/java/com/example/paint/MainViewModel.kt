package com.example.paint

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.paint.ui.theme.PathData

class MainViewModel : ViewModel() {
    // Список всех нарисованных линий
    val pathList = mutableStateListOf<PathData>()

    // Текущие параметры рисования (цвет, ширина, path)
    val currentPathData = mutableStateOf(PathData())
}
