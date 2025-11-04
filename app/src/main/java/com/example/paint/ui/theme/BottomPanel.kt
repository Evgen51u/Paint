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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun BottomPanel(
    onClick: (Color) -> Unit,
    onLineWidthChange: (Float) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onEraserClick: () -> Unit
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
        // Первый уровень: кнопки
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
                onSaveClick = { format ->
                    // Заглушка пока
                    println("Пользователь выбрал сохранение как $format")
                },
                onEraserClick = onEraserClick
            )

        }

        // ───────────────────────────────
        // Второй уровень: палитра + слайдер (в одной строке)
        // ───────────────────────────────
        if (showColorPalette) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🎨 Палитра цветов (слева)
                ColorList(
                    modifier = Modifier.weight(1f),
                    onClick = { color -> onClick(color) }
                )

                // ↔️ Небольшой отступ между палитрой и слайдером
                Spacer(modifier = Modifier.width(8.dp))

                // 🎚 Слайдер толщины линии (справа)
                CustomSlider(
                    modifier = Modifier.weight(1f),
                    onChange = { lineWidth -> onLineWidthChange(lineWidth) }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun ColorList(
    modifier: Modifier = Modifier,
    onClick: (Color) -> Unit
) {
    val colors = listOf(
        Color.Blue,
        Color.Red,
        Color.Yellow,
        Color.Green,
        Color.Black,
        Color.White
    )

    LazyRow(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(colors) { color ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onClick(color) }
                    .size(40.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    onChange: (Float) -> Unit
) {
    var position by remember { mutableStateOf(0.05f) }

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val sliderWidth = if (isLandscape)
        (configuration.screenWidthDp.dp * 0.5f)
    else
        (configuration.screenWidthDp.dp * 0.3f)

    Column(
        modifier = modifier
            .width(sliderWidth)
            .padding(horizontal = 8.dp),
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
    onSaveClick: (String) -> Unit, // теперь принимаем формат
    onEraserClick: () -> Unit
) {
    var showSaveMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ↩ Кнопка "Назад"
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            onClick = onBackClick
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Undo")
        }

        // 🎨 Кнопка "Цвета"
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            onClick = onColorToggle
        ) {
            Icon(Icons.Default.Create, contentDescription = "Color palette")
        }

        // 💾 Кнопка "Сохранить"
        Box {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White),
                onClick = { showSaveMenu = true }
            ) {
                Icon(Icons.Default.Share, contentDescription = "Save drawing")
            }

            androidx.compose.material3.DropdownMenu(
                expanded = showSaveMenu,
                onDismissRequest = { showSaveMenu = false }
            ) {
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text("Сохранить как PNG") },
                    onClick = {
                        showSaveMenu = false
                        onSaveClick("png")
                    }
                )
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text("Сохранить как SVG") },
                    onClick = {
                        showSaveMenu = false
                        onSaveClick("svg")
                    }
                )
            }
        }

        // 🧽 Кнопка "Ластик"
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            onClick = onEraserClick
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Eraser")
        }
    }
}

