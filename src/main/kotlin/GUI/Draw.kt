package org.example.GUI

import org.example.Controller.guiMode
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Toolkit
import javax.swing.JPanel

class Draw : JPanel() {
    init {
        background = Color.pink
    }
    override fun paintComponents(g: Graphics?) {
        super.paintComponents(g)
        val g2d = g as Graphics2D

        when (guiMode) {
            GuiModes.StartMenu -> startMode(g2d)
            GuiModes.JoinMenu -> joinMenu(g2d)
            GuiModes.PlayMenu -> gameMode(g2d)
            GuiModes.EndMenu -> endCard(g2d)
        }

        repaint()
        Toolkit.getDefaultToolkit().sync()
    }

    fun startMode(g2d: Graphics2D) {


    }

    fun joinMenu(g2d: Graphics2D) {

    }

    fun gameMode(g2d: Graphics2D) {

    }

    fun endCard(g2d: Graphics2D) {

    }
}