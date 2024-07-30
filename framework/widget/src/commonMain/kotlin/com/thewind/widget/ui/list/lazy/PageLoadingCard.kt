package com.thewind.widget.ui.list.lazy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thewind.widget.theme.LocalColors

@Composable
fun PageLoadingCard(color: Color = LocalColors.current.Brand_pink, loadingText: String? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp),
                strokeWidth = 5.dp,
                trackColor = Color.Transparent,
                color = color,
                strokeCap = StrokeCap.Round
            )

            loadingText?.let {
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = it, color = color, fontSize = 18.sp)
            }

        }

    }
}