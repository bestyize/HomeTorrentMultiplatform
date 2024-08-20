package com.home.user.mine


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.home.account.model.User
import com.home.kmmimage.KmmAsyncImage
import com.home.user.login.LoginScreen
import com.home.user.vm.UserViewModel
import com.thewind.resources.*
import com.thewind.utils.toDate
import com.thewind.widget.theme.LocalColors
import com.thewind.widget.ui.CommonAlertDialog
import com.thewind.widget.ui.setting.SettingItemView
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author: read
 * @date: 2023/8/20 下午2:03
 * @description:
 */

class MineScreen : Screen {
    @Composable
    override fun Content() {
        MinePage()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun Screen.MinePage() {
    val userVm = rememberScreenModel(UserViewModel.UNIQUE_KEY) { UserViewModel.INSTANCE }
    val minePageState by userVm.minePageState.collectAsState()
    val scope = rememberCoroutineScope()

    val loginScreen = remember { LoginScreen() }

    if (minePageState.showLogin) {
        BasicAlertDialog(
            onDismissRequest = {
                scope.launch {
                    userVm.closeLogin()
                }
            }, modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets(0.dp)), properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            loginScreen.Content()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(LocalColors.current.Bg2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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


    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun HeaderCard(user: User? = null, onLoginClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .background(LocalColors.current.Bg1, RoundedCornerShape(5.dp)).padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KmmAsyncImage(
            url = "https://image.uisdc.com/wp-content/uploads/2023/08/Character-avatar-20230802-1.png",
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(10.dp).clip(RoundedCornerShape(1000.dp)).size(48.dp),
        )
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.wrapContentWidth().align(Alignment.CenterStart)
            ) {
                Text(text = user?.userName.takeIf { it?.isNotBlank() == true } ?: stringResource(Res.string.not_login),
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
                            modifier = Modifier.height(5.dp).width(10.dp)
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
                    modifier = Modifier.padding(end = 10.dp).wrapContentWidth().wrapContentHeight()
                        .align(Alignment.CenterEnd).clickable {
                            onLoginClick.invoke()
                        })
            }
        }

    }
}