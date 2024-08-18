package com.home.torrent.collect.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.home.torrent.collect.model.CollectPageDialogType
import com.home.torrent.collect.vm.TorrentCollectViewModel
import com.home.torrent.widget.CopyAddressDialog
import com.home.torrent.widget.TorrentClickOption
import com.home.torrent.widget.TorrentClickOptionDialog
import com.home.torrent.widget.TorrentListView
import com.thewind.resources.Res
import com.thewind.resources.local_collect
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.ui.TitleHeader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun TorrentCollectPage() {
    val vm = viewModel(
        modelClass = TorrentCollectViewModel::class
    )

    val state by vm.localCollectPageState.collectAsState()

    val scope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().background(Color.White)
        ) {
            TitleHeader(title = stringResource(Res.string.local_collect))
            Spacer(
                modifier = Modifier.fillMaxWidth().height(0.5.dp).background(LocalColors.current.Bg2).align(
                        Alignment.BottomCenter
                    )
            )
        }

        TorrentListView(list = state.torrentList,
            collectSet = state.torrentList.toSet(),
            onClick = { data ->
                scope.launch {
                    vm.handleTorrentInfoClick(data)
                }

            },
            onCollect = { data, collect ->
                scope.launch {
                    if (collect) vm.collect(data) else vm.unCollect(data)
                }
            })
    }

    TorrentCollectPageDialog()
}

@Composable
private fun TorrentCollectPageDialog() {
    val vm: TorrentCollectViewModel = viewModel(
        modelClass = TorrentCollectViewModel::class
    )
    val scope = rememberCoroutineScope()
    val dialogState by vm.dialogState.collectAsState()
    if (dialogState.type != CollectPageDialogType.NONE) {
        when (dialogState.type) {
            CollectPageDialogType.OPTION -> TorrentClickOptionDialog(onClicked = {
                scope.launch {
                    when (it) {
                        TorrentClickOption.GET_MAGNET_URL -> vm.updateDialogState(dialogState.data)
                        TorrentClickOption.GET_TORRENT_URL -> vm.updateDialogState(
                            dialogState.data, false
                        )

                        TorrentClickOption.COLLECT_CLOUD -> vm.collectToCloud(dialogState.data)
                        TorrentClickOption.EDIT_TORRENT_TITLE -> {}
                        else -> vm.updateDialogState(null)
                    }
                }
            })

            CollectPageDialogType.ADDRESS -> CopyAddressDialog(
                address = (if (dialogState.isMagnet) dialogState.data?.magnetUrl else dialogState.data?.torrentUrl)
                    ?: ""
            ) {
                scope.launch {
                    vm.updateDialogState(null)
                }

            }

            else -> {}
        }
    }


}