package com.kappstudio.joboardgame.tools.timer

import android.graphics.Typeface
import android.text.format.DateUtils
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kappstudio.joboardgame.compose.theme.blue700
import com.kappstudio.joboardgame.compose.theme.green200
import com.kappstudio.joboardgame.compose.theme.greenLight700
import com.kappstudio.joboardgame.compose.theme.red700

@ExperimentalAnimationApi
@Composable
fun TimerScreen(timerViewModel: TimerViewModel) {
    val context = LocalContext.current
    val playPauseState = remember { mutableStateOf(true) }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (addButton, minusButton, wave, playButton, resetButton, timesUpText) = createRefs()

        AnimatedVisibility(modifier = Modifier
            .constrainAs(addButton) {
                top.linkTo(parent.top, margin = 32.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }, visible = playPauseState.value
        ) {
            TextButton(onClick = { timerViewModel.addTime(10L) },
                modifier = Modifier
                    .constrainAs(minusButton) {
                        top.linkTo(parent.top, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                content = {
                    Text(text = "+10")
                })  }

        AnimatedVisibility(modifier = Modifier
            .constrainAs(minusButton) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }, visible = playPauseState.value
        ) {
            TextButton(onClick = { timerViewModel.minusTime(10L) },
                modifier = Modifier
                    .constrainAs(minusButton) {
                        top.linkTo(parent.top, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                content = {
                    Text(text = "-10")
                })  }

        TimeWave(
            timeSpec = timerViewModel.timeState.value!! * DateUtils.SECOND_IN_MILLIS,
            playPauseState.value,
            timerViewModel,
            modifier = Modifier
                .constrainAs(wave) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        val isAlertMessageVisible by timerViewModel.alertMessage.observeAsState(initial = false)
        AnimatedVisibility(
            visible = isAlertMessageVisible,
            enter = expandIn(animationSpec = spring()),
            modifier = Modifier.constrainAs(timesUpText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = context.getString(com.kappstudio.joboardgame.R.string.timesup),
                style = MaterialTheme.typography.h3.copy(color = red700),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .border(width = 4.dp, color = red700, RoundedCornerShape(8.dp))
                    .graphicsLayer {
                        shadowElevation = 8.dp.toPx()
                    }
                    .background(color = greenLight700, RoundedCornerShape(8.dp))
                    .padding(32.dp)
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(playButton) {
                    end.linkTo(parent.end, margin = 32.dp)
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                },
            visible = playPauseState.value,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth ->
                    fullWidth + fullWidth / 2
                }
            ) + fadeIn(),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth ->
                    fullWidth + fullWidth / 2
                }
            ) + fadeOut()
        ) {
            ActionButton(
                res = com.kappstudio.joboardgame.R.drawable.ic_play_24
            ) {
                playPauseState.value = false
                timerViewModel.startTimer()
            }
        }

        AnimatedVisibility(
            visible = !playPauseState.value,
            modifier = Modifier
                .constrainAs(resetButton) {
                    start.linkTo(parent.start, margin = 32.dp)
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                }
        ) {
            ActionButton(
                res = com.kappstudio.joboardgame.R.drawable.ic_reset
            ) {
                playPauseState.value = true
                timerViewModel.stop()
            }
        }
    }
}

@Composable
fun TimeWave(
    timeSpec: Long,
    init: Boolean,
    timePackViewModel: TimerViewModel,
    modifier: Modifier
) {
    val animColor by animateColorAsState(
        targetValue = if (init) green200 else red700,
        animationSpec = TweenSpec(if (init) 0 else timeSpec.toInt(), easing = LinearEasing)
    )

    val deltaXAnim = rememberInfiniteTransition()
    val dx by deltaXAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing)
        )
    )

    val screenWidthPx = with(LocalDensity.current) {
        (LocalConfiguration.current.screenHeightDp * density) - 150.dp.toPx()
    }
    val animTranslate by animateFloatAsState(
        targetValue = if (init) 0f else screenWidthPx,
        animationSpec = TweenSpec(if (init) 0 else timeSpec.toInt(), easing = LinearEasing)
    )

    val waveHeight by animateFloatAsState(
        targetValue = if (init) 125f else 0f,
        animationSpec = TweenSpec(if (init) 0 else timeSpec.toInt(), easing = LinearEasing)
    )

    val infiniteScale = rememberInfiniteTransition()
    val animAlertScale by infiniteScale.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val waveWidth = 200
    val originalY = 150f
    val path = Path()
    val textPaint = Paint().asFrameworkPaint()

    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            translate(top = animTranslate) {
                drawPath(path = path, color = animColor)
                path.reset()
                val halfWaveWidth = waveWidth / 2
                path.moveTo(-waveWidth + (waveWidth * dx), originalY.dp.toPx())

                for (i in -waveWidth..(size.width.toInt() + waveWidth) step waveWidth) {
                    path.relativeQuadraticBezierTo(
                        halfWaveWidth.toFloat() / 2,
                        -waveHeight,
                        halfWaveWidth.toFloat(),
                        0f
                    )
                    path.relativeQuadraticBezierTo(
                        halfWaveWidth.toFloat() / 2,
                        waveHeight,
                        halfWaveWidth.toFloat(),
                        0f
                    )
                }

                path.lineTo(size.width, size.height)
                path.lineTo(0f, size.height)
                path.close()
            }

            translate(top = animTranslate * 0.92f) {
                scale(scale = if (timePackViewModel.alertState.value!!) animAlertScale else 1f) {
                    drawIntoCanvas {
                        textPaint.apply {
                            isAntiAlias = true
                            textSize = 48.sp.toPx()
                            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
                        }
                        it.nativeCanvas.drawText(
                            DateUtils.formatElapsedTime(timePackViewModel.timeState.value!!),
                            (size.width / 2) - 64.dp.toPx(),
                            120.dp.toPx(),
                            textPaint
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ActionButton(res: Int, action: () -> Unit) {
    Button(
        shape = RoundedCornerShape(36.dp),
        onClick = {
            action.invoke()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = blue700),
        modifier = Modifier.size(72.dp)
    ) {
        Image(
            painter = painterResource(id = res),
            contentDescription = "Action Button",
            modifier = Modifier.fillMaxSize()
        )
    }
}
