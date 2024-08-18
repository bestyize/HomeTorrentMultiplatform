package com.home.torrent.cloud.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.home.torrent.cloud.vm.CloudViewModel
import com.home.torrent.collect.model.TorrentInfoBean
import com.home.torrent.widget.CopyAddressDialog
import com.home.torrent.widget.TorrentClickOption
import com.home.torrent.widget.TorrentClickOptionDialog
import com.home.torrent.widget.TorrentItemTag
import com.thewind.resources.*
import com.thewind.utils.toDate
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.ui.CommonInputDialog
import com.thewind.widget.ui.TitleHeader
import com.thewind.widget.ui.list.lazy.PageLoadAllCard
import com.thewind.widget.ui.list.lazy.PageLoadErrorCard
import com.thewind.widget.ui.list.lazy.PageLoadState
import com.thewind.widget.ui.list.lazy.PageLoadingCard
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author: read
 * @date: 2023/8/21 上午12:44
 * @description:
 */

private val clickOptions = arrayOf(
    TorrentClickOption.GET_MAGNET_URL,
    TorrentClickOption.GET_TORRENT_URL,
    TorrentClickOption.EDIT_TORRENT_TITLE,
    TorrentClickOption.CANCEL
)

@Composable
@Preview
fun TorrentCloudPage() {

    val vm = viewModel(
        modelClass = CloudViewModel::class
    )
    val cloudPageState by vm.cloudPageState.collectAsState()

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TitleHeader(title = stringResource(Res.string.cloud_collect))
            CloudTorrentListView(dataList = cloudPageState.list,
                pageLoadState = cloudPageState.pageLoadState,
                onLoad = {
                    scope.launch {
                        vm.loadCloudCollectList()
                    }
                },
                onUnCollect = { index, hash ->
                    scope.launch {
                        vm.unCollectFromCloud(index, hash)
                    }
                },
                onItemClick = {
                    scope.launch {
                        vm.handleItemClick(true, it)
                    }
                })
        }
        Box(modifier = Modifier
            .padding(bottom = 80.dp, end = 15.dp)
            .align(Alignment.BottomEnd)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(100.dp))
            .background(
                color = LocalColors.current.Brand_pink, shape = RoundedCornerShape(100.dp)
            )
            .padding(10.dp)
            .clickable {
                scope.launch {
                    vm.reloadAllCollectList()
                }
            }) {
            Icon(
                Icons.Filled.Refresh,
                tint = LocalColors.current.Text_white,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (cloudPageState.showOptionDialog) {
        TorrentClickOptionDialog(options = clickOptions, onClicked = {
            scope.launch {
                when (it) {
                    TorrentClickOption.GET_MAGNET_URL -> vm.updateCopyDialogState(true, it)

                    TorrentClickOption.GET_TORRENT_URL -> vm.updateCopyDialogState(true, it)

                    TorrentClickOption.EDIT_TORRENT_TITLE -> vm.openModifyDialog()

                    else -> {}
                }
                vm.handleItemClick(showOptionDialog = false, clickOption = it)
            }

        })
    }

    val address = if (cloudPageState.clickOption == TorrentClickOption.GET_MAGNET_URL) {
        cloudPageState.selectedTorrent?.magnetUrl
    } else {
        cloudPageState.selectedTorrent?.torrentUrl
    }
    if (cloudPageState.showCopyDialog) {
        CopyAddressDialog(address = address ?: "", onCopy = {
            vm.updateCopyDialogState(false)
        })
    }

    if (cloudPageState.editDialogUiState.show) {
        CommonInputDialog(
            title = stringResource(resource = Res.string.modify_title),
            content = cloudPageState.selectedTorrent?.title ?: "",
            okText = stringResource(
                resource = Res.string.ok
            ),
            cancelText = stringResource(resource = Res.string.cancel),
            onCancel = {
                vm.closeModifyDialog()
            },
            onSubmit = { newTitle ->
                scope.launch {
                    vm.modifyTorrentTitle(newTitle)
                }
            }
        )
    }
}

@Composable
private fun CloudTorrentListView(
    dataList: List<TorrentInfoBean> = emptyList(),
    pageLoadState: PageLoadState = PageLoadState.ALL_LOADED,
    onLoad: () -> Unit = {},
    onUnCollect: (Int, String?) -> Unit = { _, _ -> },
    onItemClick: (TorrentInfoBean) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(LocalColors.current.Bg2)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = dataList.size) { index ->
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(LocalColors.current.Bg2)
                        .align(Alignment.BottomCenter)
                )
                CloudTorrentItemView(data = dataList[index], index = index, onClick = {
                    onItemClick.invoke(dataList[index])
                }, onDelete = {
                    onUnCollect.invoke(index, it)
                })
                if (index == dataList.size - 1) {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            item {
                when (pageLoadState) {
                    PageLoadState.INIT, PageLoadState.FINISH -> {
                        PageLoadingCard(loadingText = stringResource(resource = Res.string.loading))
                        LaunchedEffect(key1 = Unit, block = {
                            onLoad.invoke()
                        })
                    }

                    PageLoadState.ALL_LOADED -> {
                        PageLoadAllCard(text = stringResource(resource = Res.string.loaded_all))
                    }

                    PageLoadState.ERROR -> {
                        PageLoadErrorCard(text = stringResource(resource = Res.string.load_failed))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
private fun CloudTorrentItemView(
    data: TorrentInfoBean = TorrentInfoBean(),
    index: Int = 0,
    onClick: () -> Unit = {},
    onDelete: (String?) -> Unit = {}
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(LocalColors.current.Bg1)
        .clickable {
            onClick.invoke()
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center)
        ) {
            Text(
                text = "$index",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = LocalColors.current.Text1,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
            )

            Column(
                modifier = Modifier
                    .weight(5f)
                    .wrapContentHeight()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = data.title ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = LocalColors.current.Text1,
                    textAlign = TextAlign.Left,
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    data.date?.let {
                        TorrentItemTag(title = "日期：", data = it)
                    }
                    data.size?.let {
                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                                .height(1.dp)
                        )
                        TorrentItemTag(title = "大小：", data = it)
                    }
                    data.collectDate.let {
                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                                .height(1.dp)
                        )
                        TorrentItemTag(title = "收藏日期：", data = it.toDate())
                    }
                }
            }
            Icon(imageVector = Icons.Default.Delete,
                contentDescription = "收藏",
                tint = Color.LightGray,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onDelete.invoke(data.hash)
                    })
        }
    }

}