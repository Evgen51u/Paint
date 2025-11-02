package com.example.paint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.input.pointer.pointerInput
import com.example.paint.ui.theme.BottomPanel
import com.example.paint.ui.theme.PaintTheme
import com.example.paint.ui.theme.PathData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val pathData = remember {
                mutableStateOf(PathData())
            }
            val pathList = remember {
                mutableStateListOf(PathData())
            }
            PaintTheme {
                Column {
                    PaintCanvas(pathData, pathList)
                    BottomPanel(
                        { color ->
                            pathData.value = pathData.value.copy(
                                color = color
                            )

                        },
                        {lineWidth ->
                            pathData.value = pathData.value.copy(
                                lineWidth = lineWidth
                            )
                        },
                    {
                        pathList.removeIf { pathD ->
                            pathList[pathList.size - 1] == pathD//сравнение совпадений (для удаления дубликатов)
                        } //отмена последнего (удаление из массива)

                    },
                    { // Новый обработчик для Save
                        // Здесь будет логика сохранения рисунка
                        // Например: saveDrawingToGallery(pathList)
                        println("Save button clicked!")
                    }
                    )

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
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .pointerInput(true){
                detectDragGestures( //обработка пути при каждом касании
                    onDragStart = {
                        tempPath = Path() //добавление при рисовании
                    },
                    onDragEnd = {
                        pathList.add( //для каждой линии один data класс
                            pathData1.value.copy(
                                path = tempPath //добавление при завершении
                            )
                        )
                    }
                    //последний элемент дублируется
                ) { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )
                    if (pathList.size > 0){ //очищаем лишнее, оставляя финальный класс
                        pathList.removeAt(pathList.size - 1)
                    }
                    pathList.add( //при рисовании линия добавляется в pathList
                        pathData1.value.copy(
                            path = tempPath
                        )
                    )

                }
            }
    ) {
        pathList.forEach { pathData -> //цикл по очереди выдает все сохраненные пути
            drawPath(
                pathData.path,
                color = pathData.color,
                style = Stroke(pathData.lineWidth,
                    cap = StrokeCap.Round) //ширина пера и стиль

            )
        }

    }
}