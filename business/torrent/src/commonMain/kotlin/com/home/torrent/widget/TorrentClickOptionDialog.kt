package com.home.torrent.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thewind.widget.theme.LocalColors


/**
 * @author: read
 * @date: 2023/8/13 下午11:04
 * @description:
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TorrentClickOptionDialog(
    onClicked: (TorrentClickOption) -> Unit,
    options: Array<TorrentClickOption> = TorrentClickOption.entries.toTypedArray()
) {
    ModalBottomSheet(onDismissRequest = {
        onClicked.invoke(TorrentClickOption.CANCEL)
    }, content = {
        Spacer(
            modifier = Modifier.padding(vertical = 10.dp).align(Alignment.CenterHorizontally).fillMaxWidth(0.8f)
                .height(0.5.dp).background(LocalColors.current.Bg2)
        )
        options.forEach { option ->
            Text(text = option.value,
                color = LocalColors.current.Text1,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(0.8f).wrapContentHeight()
                    .align(Alignment.CenterHorizontally).clickable(indication = null, interactionSource = remember {
                        MutableInteractionSource()
                    }) {
                        onClicked.invoke(option)
                    })
            Spacer(
                modifier = Modifier.padding(vertical = 10.dp).align(Alignment.CenterHorizontally).fillMaxWidth(0.8f)
                    .height(0.5.dp)
                    .background(if (option != TorrentClickOption.CANCEL) LocalColors.current.Bg2 else Color.Transparent)
            )

        }
    }, containerColor = LocalColors.current.Bg1, modifier = Modifier.fillMaxWidth().wrapContentHeight()
    )
}

internal enum class TorrentClickOption(val value: String) {
    GET_MAGNET_URL("获取磁力链接"), GET_TORRENT_URL(
        "获取种子地址"
    ),
    COLLECT_CLOUD("收藏到云端"), EDIT_TORRENT_TITLE("修改标题"), CANCEL(
        "取消"
    )
}