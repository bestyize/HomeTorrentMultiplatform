package torrent.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thewind.kmmplayer.KmmPlayer
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.theme.ThemeId
import com.thewind.widget.theme.ThemeManager

@Composable
fun TorrentSearchPage(index: Int) {
    var isNight by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().clickable {
        isNight = isNight.not()
        ThemeManager.updateTheme(if (isNight) ThemeId.NIGHT else ThemeId.DAY)
    }, contentAlignment = Alignment.Center) {
        if (index == 1) {
            KmmPlayer(
                modifier = Modifier.fillMaxSize(),
                url = "https://upos-sz-static.bilivideo.com/ssaxcode/ee/g5/n240803sa2gzcmfl4jdepc1isn0tg5ee-1-SPLASH.mp4"
            )
        }
        Text("Search Torrents - $index", color = LocalColors.current.Text1)
    }
}