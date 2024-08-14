package com.thewind.kmmplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun KmmPlayer(modifier: Modifier = Modifier, url: String)