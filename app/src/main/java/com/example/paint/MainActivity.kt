package com.example.paint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.example.paint.ui.theme.PaintTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaintTheme {
                PaintCanvas()


            }
        }
    }
}




@Composable
fun PaintCanvas() {
    val tempPath = Path()
    val path = remember {
        mutableStateOf(Path())
    }
    Canvas(
        modifier = Modifier.fillMaxSize()
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
            color = Color.Blue,
            style = Stroke(5f)

        )

    }
}