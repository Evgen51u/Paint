package com.example.paint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.paint.ui.theme.BottomPanel
import com.example.paint.ui.theme.PaintTheme
import com.example.paint.ui.theme.PathData
import androidx.compose.ui.graphics.Color
import androidx.activity.viewModels
import com.example.paint.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val pathData = remember { mutableStateOf(PathData()) }
            //val pathList = remember { mutableStateListOf(PathData()) }

            val viewModel: MainViewModel by viewModels()

            PaintTheme {
                // Используем Box для наложения элементов
                Box(modifier = Modifier.fillMaxSize()) {

                    // 🎨 Холст для рисования (нижний слой)
                    PaintCanvas(viewModel.currentPathData, viewModel.pathList)

                    // 🧭 Панель управления (верхний слой)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter) // приклеить к верху
                            .padding(top = 40.dp)
                    ) {
                        BottomPanel(
                            { color ->
                                viewModel.currentPathData.value =
                                    viewModel.currentPathData.value.copy(color = color)
                            },
                            { lineWidth ->
                                viewModel.currentPathData.value =
                                    viewModel.currentPathData.value.copy(lineWidth = lineWidth)
                            },
                            {
                                if (viewModel.pathList.isNotEmpty()) {
                                    viewModel.pathList.removeAt(viewModel.pathList.size - 1)
                                }
                            },
                            {
                                println("Save button clicked!")
                            },
                            {
                                // 🧽 Ластик: просто меняем цвет на цвет фона
                                viewModel.currentPathData.value =
                                    viewModel.currentPathData.value.copy(color = Color(0xFFFFFBFE))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaintCanvas(pathData1: MutableState<PathData>, pathList: SnapshotStateList<PathData>) {
    var tempPath = Path()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp) // 👈 отступ, чтобы не рисовать по панели
            .navigationBarsPadding()
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {
                        tempPath = Path()
                    },
                    onDragEnd = {
                        pathList.add(
                            pathData1.value.copy(
                                path = tempPath
                            )
                        )
                    }
                ) { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )

                    if (pathList.size > 0) {
                        pathList.removeAt(pathList.size - 1)
                    }

                    pathList.add(
                        pathData1.value.copy(
                            path = tempPath
                        )
                    )
                }
            }
    ) {
        pathList.forEach { pathData ->
            drawPath(
                pathData.path,
                color = pathData.color,
                style = Stroke(
                    pathData.lineWidth,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
