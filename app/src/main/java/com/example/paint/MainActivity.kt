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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.paint.ui.theme.BottomPanel
import com.example.paint.ui.theme.PaintTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val colorState = remember {
                mutableStateOf(Color.Blue)
            }
            PaintTheme {
                Column {
                    PaintCanvas(colorState)
                    BottomPanel(){ color ->
                        colorState.value = color

                    }
                }



            }
        }
    }
}




@Composable
fun PaintCanvas(colorState: MutableState<Color>) {
    val tempPath = Path()
    val path = remember {
        mutableStateOf(Path())
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .pointerInput(true){
                detectDragGestures { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )
                    path.value = Path().apply{ //сохранение линий на экране
                        addPath(tempPath)
                    }
                }
            }
    ) {
        drawPath(
            path.value,
            color = colorState.value,
            style = Stroke(5f)

        )

    }
}