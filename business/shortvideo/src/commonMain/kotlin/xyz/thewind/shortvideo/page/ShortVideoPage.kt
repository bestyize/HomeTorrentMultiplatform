package xyz.thewind.shortvideo.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.thewind.kmmplayer.KmmPlayer
import xyz.thewind.shortvideo.vm.ShortVideoPageViewModel

class ShortVideoScreen : Screen {
    @Composable
    override fun Content() {
        ShortVideoPage()
    }

}

@Composable
private fun Screen.ShortVideoPage() {
    val vm = rememberScreenModel { ShortVideoPageViewModel() }
    val state by vm.state.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { state.videos.size })
    VerticalPager(state = pagerState, modifier = Modifier.fillMaxSize().background(color = Color.Black)) { page ->
        KmmPlayer(modifier = Modifier.fillMaxSize(), state.videos[page].url)
    }
}