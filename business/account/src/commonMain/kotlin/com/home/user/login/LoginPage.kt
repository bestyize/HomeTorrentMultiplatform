package com.home.user.login


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.home.user.model.LoginPageStage
import com.home.user.vm.UserViewModel
import com.thewind.resources.*
import com.thewind.widget.theme.LocalColors
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author: read
 * @date: 2023/8/18 上午12:40
 * @description:
 */


class LoginScreen : Screen {
    @Composable
    override fun Content() {
        LoginPage()
    }

}

@Composable
@Preview
private fun Screen.LoginPage() {

    val vm = rememberScreenModel(UserViewModel.UNIQUE_KEY) { UserViewModel.INSTANCE }

    val loginPageState by vm.loginPageStage.collectAsState()

    val maxWidthScale = 0.8f

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(LocalColors.current.Bg1)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Icon(imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = LocalColors.current.Text1,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                ).padding(20.dp).clickable {
                    vm.closeLogin()
                })
        }

        Column(
            modifier = Modifier.fillMaxWidth(maxWidthScale).align(Alignment.Center).wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(Res.string.app_name),
                fontWeight = FontWeight.Bold,
                color = LocalColors.current.Brand_pink,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxWidth(maxWidthScale / 2).align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (loginPageState.pageStage == LoginPageStage.LOGIN || loginPageState.pageStage == LoginPageStage.REGISTER) {
                OutlinedTextField(value = loginPageState.userName, onValueChange = {
                    scope.launch {
                        vm.updateUserNameText(it)
                    }
                }, label = {
                    Text(
                        text = stringResource(Res.string.username),
                        color = LocalColors.current.Brand_pink,
                        fontWeight = FontWeight.Bold
                    )
                }, colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LocalColors.current.Brand_pink,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = LocalColors.current.Bg2,
                    unfocusedContainerColor = LocalColors.current.Bg2,
                    focusedTextColor = LocalColors.current.Text1,
                    unfocusedTextColor = LocalColors.current.Text1
                ), modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(value = loginPageState.password, onValueChange = {
                scope.launch {
                    vm.updatePasswordText(it)
                }
            }, label = {
                Text(
                    text = if (loginPageState.pageStage == LoginPageStage.MODIFY_PASSWORD) stringResource(
                        Res.string.new_password
                    ) else stringResource(
                        Res.string.password
                    ), color = LocalColors.current.Brand_pink, fontWeight = FontWeight.Bold
                )
            }, colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = LocalColors.current.Brand_pink,
                focusedContainerColor = LocalColors.current.Bg2,
                unfocusedContainerColor = LocalColors.current.Bg2,
                focusedTextColor = LocalColors.current.Text1,
                unfocusedTextColor = LocalColors.current.Text1
            ), modifier = Modifier.fillMaxWidth()
            )

            if (loginPageState.pageStage == LoginPageStage.REGISTER || loginPageState.pageStage == LoginPageStage.MODIFY_PASSWORD) {
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(value = loginPageState.email, onValueChange = {
                    scope.launch {
                        vm.updateEmailText(it)
                    }
                }, label = {
                    Text(
                        text = stringResource(resource = Res.string.email),
                        color = LocalColors.current.Brand_pink,
                        fontWeight = FontWeight.Bold
                    )
                }, colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = LocalColors.current.Brand_pink,
                    focusedContainerColor = LocalColors.current.Bg2,
                    unfocusedContainerColor = LocalColors.current.Bg2,
                    focusedTextColor = LocalColors.current.Text1,
                    unfocusedTextColor = LocalColors.current.Text1
                ), modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(value = loginPageState.verifyCode, onValueChange = {
                    scope.launch {
                        vm.updateVerifyCode(it)
                    }
                }, label = {
                    Text(
                        text = stringResource(Res.string.verify_code),
                        color = LocalColors.current.Brand_pink,
                        fontWeight = FontWeight.Bold
                    )
                }, trailingIcon = {
                    Text(text = stringResource(Res.string.send),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalColors.current.Text1,
                        modifier = Modifier.padding(end = 20.dp).clickable {
                            scope.launch {
                                vm.sendVerifyCode()
                            }

                        })
                }, colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = LocalColors.current.Brand_pink,
                    focusedContainerColor = LocalColors.current.Bg2,
                    unfocusedContainerColor = LocalColors.current.Bg2,
                    focusedTextColor = LocalColors.current.Text1,
                    unfocusedTextColor = LocalColors.current.Text1
                ), modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    scope.launch {
                        vm.handleLoginClick()
                    }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LocalColors.current.Brand_pink)
            ) {
                val txt = when (loginPageState.pageStage) {
                    LoginPageStage.REGISTER -> stringResource(Res.string.register)
                    LoginPageStage.LOGIN -> stringResource(Res.string.login)
                    LoginPageStage.MODIFY_PASSWORD -> stringResource(Res.string.modify_or_find_password)
                }
                Text(
                    text = txt, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LocalColors.current.Text_white
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            ) {
                Text(text = if (loginPageState.pageStage == LoginPageStage.REGISTER) stringResource(
                    Res.string.switch_login
                ) else stringResource(
                    Res.string.switch_register
                ),
                    color = LocalColors.current.Text1,
                    modifier = Modifier.wrapContentWidth().align(Alignment.CenterEnd).clickable {
                        if (loginPageState.pageStage == LoginPageStage.REGISTER) {
                            vm.updatePageStage(LoginPageStage.LOGIN)
                        } else {
                            vm.updatePageStage(LoginPageStage.REGISTER)
                        }

                    })

                Text(text = stringResource(Res.string.modify_or_find_password),
                    color = LocalColors.current.Text_white,
                    modifier = Modifier.wrapContentWidth().align(Alignment.CenterStart).clickable {
                        scope.launch {
                            vm.updatePageStage(LoginPageStage.MODIFY_PASSWORD)
                        }
                    })

            }
        }

        Text(
            text = stringResource(Res.string.login_to_unlock_more_function),
            color = LocalColors.current.Text1,
            modifier = Modifier.padding(bottom = 20.dp).align(Alignment.BottomCenter)
        )
    }
}