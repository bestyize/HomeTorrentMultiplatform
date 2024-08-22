package com.thewind.widget.ui.toast

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JFrame
import javax.swing.JLabel


@OptIn(DelicateCoroutinesApi::class)
actual fun toast(msg: String?) {
    msg ?: return
    GlobalScope.launch {
        JFrame().apply {
            layout = GridBagLayout()
            isUndecorated = true
            contentPane.background = Color(0xFF6699)
            size = Dimension(300, 100)
            val cx = Toolkit.getDefaultToolkit().screenSize.width / 2
            val cy = Toolkit.getDefaultToolkit().screenSize.height / 2
            setLocation(cx - size.width / 2, cy - size.height / 2)
            add(JLabel(msg).apply {
                foreground = Color.WHITE
                font = font.deriveFont(Font.BOLD, 16f)
            })
            addComponentListener(object : ComponentAdapter() {
                override fun componentResized(e: ComponentEvent) {
                    shape = RoundRectangle2D.Double(
                        0.0, 0.0, width.toDouble(), height.toDouble(), 20.0, 20.0
                    )
                }
            })
            isAlwaysOnTop = true
        }.apply {
            isVisible = true
            delay(1800)
            var d = 0.4
            while (d > 0.2) {
                delay(100)
                opacity = d.toFloat()
                d -= 0.1
            }
            isVisible = false
        }
    }

}


private class ToastMessage(msg: String) : JFrame() {
    init {
        isUndecorated = true
        layout = GridBagLayout()
        contentPane.background = Color(0xFF6699)
        setLocationRelativeTo(null)
        setSize(300, 50)
        val cx = Toolkit.getDefaultToolkit().screenSize.width / 2
        val cy = Toolkit.getDefaultToolkit().screenSize.height / 2
        setLocation(cx - size.width / 2, cy - size.height / 2)
        add(JLabel(msg))

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                shape = RoundRectangle2D.Double(
                    0.0, 0.0, width.toDouble(), height.toDouble(), 20.0, 20.0
                )
            }
        })
        isAlwaysOnTop = true
    }

    suspend fun display() {
        try {
            opacity = 1f
            isVisible = true
            delay(1500)

            var d = 0.4
            while (d > 0.2) {
                delay(100)
                opacity = d.toFloat()
                d -= 0.1
            }
            isVisible = false
        } catch (e: Exception) {
            println(e.message)
        }
    }
}