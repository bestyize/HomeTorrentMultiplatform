package com.thewind.widget.ui.toast

import android.widget.Toast
import com.home.baseapp.app.HomeApp

actual fun toast(msg: String?) {
    Toast.makeText(HomeApp.context, msg, Toast.LENGTH_SHORT).show()
}