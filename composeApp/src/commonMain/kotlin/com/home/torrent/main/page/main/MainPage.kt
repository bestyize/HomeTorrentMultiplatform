package com.home.torrent.main.page.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.home.torrent.cloud.page.TorrentCloudScreen
import com.home.torrent.collect.page.TorrentCollectScreen
import com.home.torrent.main.page.main.vm.HomeViewModel
import com.home.torrent.search.page.TorrentSearchScreen
import com.home.user.mine.MineScreen
import com.thewind.kmmplayer.KmmPlayer
import com.thewind.widget.theme.LocalColors
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import xyz.thewind.shortvideo.page.ShortVideoScreen


class MainScreen : Screen {

    @Composable
    override fun Content() {
        MainPage()
    }
}

@Composable
@Preview
private fun MainScreen.MainPage() {
    val vm = rememberScreenModel { HomeViewModel() }
    val state by vm.state.collectAsState()

    val pagerState = rememberPagerState {
        state.tabs.size
    }

    val scope = rememberCoroutineScope()

    val searchScreen = remember { TorrentSearchScreen() }
    val collectionScreen = remember { TorrentCollectScreen() }
    val cloudScreen = remember { TorrentCloudScreen() }
    val mineScreen = remember { MineScreen() }
    val shortVideoScreen = remember { ShortVideoScreen() }

    Box(modifier = Modifier.fillMaxSize().background(LocalColors.current.Bg1).statusBarsPadding()) {

        HorizontalPager(state = pagerState) { page ->
            Box(
                modifier = Modifier.padding(bottom = 60.dp).fillMaxSize()
            ) {
                when (page) {
                    0 -> searchScreen.Content()
                    1 -> collectionScreen.Content()
                    2 -> cloudScreen.Content()
                    3 -> mineScreen.Content()
                    4 -> shortVideoScreen.Content()
                }
            }
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = {},
            containerColor = LocalColors.current.Bg3,
            divider = {},
            modifier = Modifier.align(Alignment.BottomCenter).shadow(elevation = 3.dp).height(60.dp).fillMaxWidth()
        ) {
            state.tabs.forEachIndexed { index, title ->
                val isSelected = pagerState.currentPage == index
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) LocalColors.current.Brand_pink else LocalColors.current.Text1,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = if (isSelected) 18.sp else 17.sp,
                    modifier = Modifier.clickable(onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }, indication = null, interactionSource = remember { MutableInteractionSource() })
                )
            }
        }
    }
}
