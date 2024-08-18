package com.home.user.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
import com.thewind.widget.ui.setting.SettingItemView
import com.home.account.model.User
import com.home.user.login.LoginPage
import com.home.user.vm.UserViewModel
import com.thewind.resources.*
import com.thewind.utils.toDate
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.ui.CommonAlertDialog
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author: read
 * @date: 2023/8/20 下午2:03
 * @description:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MinePage() {
    val userVm = viewModel(
        modelClass = UserViewModel::class
    )
    val minePageState by userVm.minePageState.collectAsState()
    val scope = rememberCoroutineScope()

    if (minePageState.showLogin) {
        BasicAlertDialog(
            onDismissRequest = {
                scope.launch {
                    userVm.closeLogin()
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(0.dp)),
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            LoginPage {
                scope.launch {
                    userVm.closeLogin()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.current.Bg2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        HeaderCard(user = minePageState.user, onLoginClick = {
            scope.launch {
                userVm.openLogin()
            }
        })
        Spacer(modifier = Modifier.height(20.dp))
        SettingItemView(title = stringResource(Res.string.setting), icon = Icons.Default.Settings) {
            //router.navigate("ht://setting")
        }

        if (minePageState.user != null) {

            if (minePageState.showLogout) {
                CommonAlertDialog(title = stringResource(Res.string.notice),
                    content = stringResource(Res.string.do_you_want_to_logout),
                    okText = stringResource(Res.string.ok),
                    cancelText = stringResource(Res.string.cancel),
                    onCancel = {
                        userVm.closeLogoutWaring()
                    },
                    onOk = {
                        userVm.logout()
                    })
            }
            Spacer(modifier = Modifier.height(20.dp))
            SettingItemView(
                title = stringResource(Res.string.logout), icon = Icons.AutoMirrored.Filled.ExitToApp
            ) {
                userVm.showLogoutWaring()
            }
        }
//        val noticeList = remember {
//            emptyList()
//        }

//        if (!noticeList.isNullOrEmpty()) {
//            noticeList.filter { !it.title.isNullOrBlank() }.forEach { option ->
//
//                option.title?.let { title ->
//                    Spacer(modifier = Modifier.height(20.dp))
//                    SettingItemView(
//                        title = title, icon = Icons.AutoMirrored.Filled.ArrowForward
//                    ) {
//                        option.actionUrl.takeIf { !it.isNullOrBlank() }?.let {
//                            //activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
//                        }
//
//                    }
//                }
//
//            }
//        }


    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun HeaderCard(user: User? = null, onLoginClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(LocalColors.current.Bg1, RoundedCornerShape(5.dp))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current).data(user?.icon)
//                .error(Res.drawable.logo).build(),
//            placeholder = painterResource(Res.drawable.logo),
//            alignment = Alignment.Center,
//            modifier = Modifier
//                .padding(10.dp)
//                .clip(RoundedCornerShape(1000.dp))
//                .size(48.dp),
//            contentDescription = null
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterStart)
            ) {
                Text(text = user?.userName.takeIf { it?.isNotBlank() == true }
                    ?: stringResource(Res.string.not_login),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LocalColors.current.Text1)
                Spacer(modifier = Modifier.height(5.dp))
                FlowRow(modifier = Modifier.wrapContentSize()) {
                    user?.uid?.let {
                        Text(
                            text = stringResource(Res.string.uid, user.uid),
                            fontSize = 12.sp,
                            color = LocalColors.current.Text1
                        )
                    }

                    user?.registerTime.toDate().takeIf { it.isNotBlank() }?.let {
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                                .width(10.dp)
                        )
                        Text(
                            text = stringResource(
                                Res.string.register_date, user?.registerTime.toDate()
                            ), fontSize = 12.sp, color = LocalColors.current.Text1
                        )
                    }

                }


            }
            if (user == null) {
                Text(text = stringResource(Res.string.login_or_register),
                    color = LocalColors.current.Text1,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .align(Alignment.CenterEnd)
                        .clickable {
                            onLoginClick.invoke()
                        })
            }
        }

    }
}