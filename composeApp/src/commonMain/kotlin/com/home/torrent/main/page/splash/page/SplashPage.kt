package com.home.torrent.main.page.splash.page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.ui.TitleHeader
import hometorrentmultiplatform.composeapp.generated.resources.Res
import hometorrentmultiplatform.composeapp.generated.resources.logo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        SplashPage()
    }
}

@Composable
private fun SplashPage() {
    val state = splashLogoAnimationStateHolder()
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.currentOrThrow

    val countDownState by remember {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White).statusBarsPadding()
    ) {
        var timeLeft by remember {
            mutableIntStateOf(2)
        }
        SkipButton(modifier = Modifier.align(Alignment.TopEnd), skipText = "Skip" + " $timeLeft", onClose = {
            if (countDownState) {
                scope.launch {
                    navigator.pop()
                }
            }
        })
        if (timeLeft == 0) {
            LaunchedEffect(Unit) {
                navigator.pop()
            }
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
            modifier = Modifier.wrapContentSize().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(Res.drawable.logo),
                contentDescription = "",
                modifier = Modifier.size(state.logoSize.dp).graphicsLayer {
                    cameraDistance = 10000f
                    rotationX = state.rotateAngle
                    rotationZ = state.rotateAngle
                    rotationY = state.rotateAngle
                })
            TitleHeader(
                "TorrentHome", color = LocalColors.current.Brand_pink, backgroundColor = Color.Transparent
            )
        }

    }

}


@Composable
private fun SkipButton(
    modifier: Modifier = Modifier, skipText: String = "Skip", onClose: () -> Unit = {}
) {
    Box(modifier = modifier.padding(15.dp).wrapContentSize()
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
            targetValue = if (playRotate) 180f else 0f, label = "logoSize", animationSpec = tween(durationMillis = 1200)
        ).value, rotateAngle = animateFloatAsState(
            targetValue = if (playRotate) 0f else 180f,
            animationSpec = tween(durationMillis = 1200),
            label = "logoRotate"
        ).value
    )
}

private data class SplashLogoAnimationState(val logoSize: Float = 0f, val rotateAngle: Float = 0f)