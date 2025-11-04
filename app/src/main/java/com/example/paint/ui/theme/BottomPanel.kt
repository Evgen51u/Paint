package com.example.paint.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomPanel(
    onClick: (Color) -> Unit,
    onLineWidthChange: (Float) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // состояние для отображения палитры
    var showColorPalette by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ───────────────────────────────
        // Первый уровень: кнопки (в одной строке)
        // ───────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonPanel(
                onBackClick = onBackClick,
                onColorToggle = { showColorPalette = !showColorPalette },
                onSaveClick = onSaveClick
            )
        }

        // ───────────────────────────────
        // Второй уровень: палитра и слайдер (на одном уровне, ниже)
        // ───────────────────────────────
        if (showColorPalette) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ColorList { color ->
                    onClick(color)
                }

                Spacer(modifier = Modifier.height(8.dp))

                CustomSlider { lineWidth ->
                    onLineWidthChange(lineWidth)
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}

// настройка 1: выбор цвета
@Composable
fun ColorList(onClick: (Color) -> Unit) {
    val colors = listOf( // массив цветов
        Color.Blue,
        Color.Red,
        Color.Yellow,
        Color.Green,
        Color.Black,
        Color.White
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        items(colors) { color ->
            Box( // контейнер
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        onClick(color)
                    }
                    .size(40.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
fun CustomSlider(onChange: (Float) -> Unit) {
    var position by remember {
        mutableStateOf(0.05f)
    }
    Column(
        modifier = Modifier
            .width(200.dp), // Фиксированная ширина для слайдера
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Width: ${(position * 100).toInt()}")
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = position,
            onValueChange = {
                val tempPos = if (it > 0) it else 0.01f
                position = tempPos
                onChange(tempPos * 100)
            }
        )
    }
}

@Composable
fun ButtonPanel(
    onBackClick: () -> Unit,
    onColorToggle: () -> Unit,
    onSaveClick: () -> Unit // параметр для сохранения
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Отступ между кнопками
    ) {
        // Кнопка "Назад"
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            onClick = onBackClick
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Undo"
            )
        }

        // Кнопка "Цвета"
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            onClick = onColorToggle
        ) {
            Icon(
                Icons.Default.Create,
                contentDescription = "Color palette"
            )
        }

        // Кнопка "Сохранить"
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            onClick = onSaveClick
        ) {
            Icon(
                Icons.Default.Share,
                contentDescription = "Save drawing"
            )
        }
    }
}
