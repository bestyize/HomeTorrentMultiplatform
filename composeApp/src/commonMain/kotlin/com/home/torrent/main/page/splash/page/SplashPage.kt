package com.home.torrent.main.page.splash.page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.ui.TitleHeader
import hometorrentmultiplatform.composeapp.generated.resources.Res
import hometorrentmultiplatform.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashPage(onClose: () -> Unit = {}) {
    val state = splashLogoAnimationStateHolder()

    var countDownState by remember {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        var timeLeft by remember {
            mutableIntStateOf(2)
        }
        SkipButton(
            modifier = Modifier.align(Alignment.TopEnd),
            skipText = "Skip" + " $timeLeft",
            onClose = {
                if (countDownState) {
                    onClose.invoke()
                }
            }
        )
        if (timeLeft == 0) {
            onClose.invoke()
        }
        LaunchedEffect(key1 = Unit) {
            while (timeLeft > 0) {
                delay(1000)
                if (countDownState) {
                    timeLeft -= 1
                }

            }
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "",
                modifier = Modifier
                    .size(state.logoSize.dp)
                    .graphicsLayer {
                        cameraDistance = 10000f
                        rotationX = state.rotateAngle
                        rotationZ = state.rotateAngle
                        rotationY = state.rotateAngle
                    }
            )
            TitleHeader(
                "TorrentHome",
                color = LocalColors.current.Brand_pink,
                backgroundColor = Color.Transparent
            )
        }

    }

}


@Composable
private fun SkipButton(
    modifier: Modifier = Modifier,
    skipText: String = "Skip",
    onClose: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(15.dp)
            .wrapContentSize()
            .background(color = remember { Color(0x7F333333) }, shape = RoundedCornerShape(115.dp))
            .clickable(indication = null, interactionSource = remember {
                MutableInteractionSource()
            }, onClick = {
                onClose.invoke()
            })
    ) {
        Text(
            text = skipText,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun splashLogoAnimationStateHolder(): SplashLogoAnimationState {
    var playRotate by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        playRotate = true
    }
    return SplashLogoAnimationState(
        logoSize = animateFloatAsState(
            targetValue = if (playRotate) 180f else 0f,
            label = "logoSize",
            animationSpec = tween(durationMillis = 1200)
        ).value,
        rotateAngle = animateFloatAsState(
            targetValue = if (playRotate) 0f else 180f,
            animationSpec = tween(durationMillis = 1200),
            label = "logoRotate"
        ).value
    )
}

private data class SplashLogoAnimationState(val logoSize: Float = 0f, val rotateAngle: Float = 0f)