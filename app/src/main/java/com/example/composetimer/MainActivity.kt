    package com.example.composetimer

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.Canvas
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.size
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.currentRecomposeScope
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.geometry.Offset
    import androidx.compose.ui.geometry.Size
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.PointMode
    import androidx.compose.ui.graphics.StrokeCap
    import androidx.compose.ui.graphics.drawscope.Stroke
    import androidx.compose.ui.layout.onSizeChanged
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.Dp
    import androidx.compose.ui.unit.IntSize
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.composetimer.ui.theme.ComposeTimerTheme
    import kotlinx.coroutines.delay
    import kotlin.math.PI
    import kotlin.math.cos
    import kotlin.math.sin

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                ComposeTimerTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xff101010)
                    ) {
                            Box(contentAlignment = Alignment.Center){
                                    Timer(
                                        totalTime = 60L * 1000L,
                                        handleColor = Color.Green,
                                        inactiveBarColor = Color.DarkGray,
                                        activeBarColor = Color(0xff37B900),
                                        modifier = Modifier.size(200.dp)
                                    )
                            }
                    }
                }
            }
        }
    }

    @Composable
    fun Timer(
        totalTime:Long,
        handleColor:Color,
        inactiveBarColor:Color,
        activeBarColor:Color,
        modifier: Modifier=Modifier,
        initialValue:Float=1f,
        strokeWith:Dp=5.dp,

    ){

        var size by remember {
            mutableStateOf(IntSize.Zero)
        }

        var value by remember {
            mutableStateOf(initialValue)
        }

        var currentTime by remember {
            mutableStateOf(totalTime)
        }

        var isTimeRunning by remember {
            mutableStateOf(true)
        }
        //the key inside launchEffect means: Whenever the key changes, it will re run the code inside.
        LaunchedEffect(key1 = currentTime, key2 = isTimeRunning){
                if (currentTime>0 && isTimeRunning){
                    delay(100L)
                    currentTime -=100L
                    value=currentTime/totalTime.toFloat()
                }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.onSizeChanged {
                size=it
            }
        ) {

            Canvas(modifier = modifier){
                //startAngle =  the starting angle,
                // sweepAngle = for how many degrees we want this to be long
                //useCenter will prevent the ends to be connected to the center. will test this latter
                    drawArc(
                        color = inactiveBarColor,
                        startAngle = -215f,
                        sweepAngle = 250f,
                        useCenter = false,
                        size = Size(size.width.toFloat(),size.height.toFloat()),
                        style = Stroke(strokeWith.toPx(), cap = StrokeCap.Round)//make sure that the ends are round.
                    )
                drawArc(
                    color = activeBarColor,
                    startAngle = -215f,
                    sweepAngle = 250f * value,
                    useCenter = false,
                    size = Size(size.width.toFloat(),size.height.toFloat()),
                    style = Stroke(strokeWith.toPx(), cap = StrokeCap.Round)//make sure that the ends are round.
                )

                val center = Offset(size.width/2f,size.height/2f)   // gets the center
                val beta = (250f * value + 145f) * (PI/180f).toFloat() //the angle between our center and the handle.
                val r = size.width/2f
                val a = cos(beta) * r
                val b = sin(beta) * r

                drawPoints(
                    listOf(Offset(center.x + a, center.y + b)),
                    pointMode = PointMode.Points,
                    color = handleColor,
                    strokeWidth = (strokeWith * 3f).toPx(),
                    cap = StrokeCap.Round
                )
            }

            Text(
                text=(currentTime/1000L).toString(), //hour time is millis and we want it in seconds
            fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(onClick = {
                    if(currentTime<=0L){
                        currentTime=totalTime
                        isTimeRunning=true
                    }else{
                        isTimeRunning= !isTimeRunning
                    }
            },modifier=Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(!isTimeRunning || currentTime <= 0L){
                Color.Green
                }else{
                    Color.Red
                }
            )) {
                Text(
                    text = if (isTimeRunning && currentTime >= 0L) "Stop"
                            else if (isTimeRunning && currentTime >= 20) "Start"
                    else "Restart"
                )
            }

        }

    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ComposeTimerTheme {

        }
    }