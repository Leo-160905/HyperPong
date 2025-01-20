package client.gui

import client.controller.racketPos
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Toolkit
import java.lang.Math.toRadians
import javax.swing.JPanel

class Draw : JPanel() {
    init {
        background = Color.black
    }
    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        g2d.color = Color.white
        g2d.translate(500,500)
        g2d.drawOval(-50,-50,100,100)

        g2d.rotate(toRadians(racketPos * 1.0))
        g2d.fillRect(-50,50,100,10)
        g2d.translate(-500,-500)
    }
}