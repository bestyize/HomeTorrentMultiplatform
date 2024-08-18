package com.thewind.widget.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thewind.widget.theme.LocalColors
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author: read
 * @date: 2023/8/20 下午3:42
 * @description:
 */

@Composable
@Preview
fun SettingItemView(
    title: String = "",
    titleColor: Color = LocalColors.current.Text1,
    icon: ImageVector = Icons.Default.KeyboardArrowRight,
    onClick: () -> Unit = {}
) {
    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = LocalColors.current.Bg1, shape = RoundedCornerShape(5.dp))
        .clickable {
            onClick.invoke()
        }) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = titleColor,
            modifier = Modifier
                .padding(15.dp)
                .wrapContentSize()
        )
        Icon(
            imageVector = icon,
            tint = LocalColors.current.Text1,
            contentDescription = "",
            modifier = Modifier
                .padding(end = 15.dp)
                .wrapContentSize()
                .align(
                    Alignment.CenterEnd
                )
        )
    }
}