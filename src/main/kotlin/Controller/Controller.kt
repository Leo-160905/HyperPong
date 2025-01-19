package org.example.Controller

import org.example.GUI.Draw
import org.example.GUI.Gui
import org.example.GUI.GuiModes


val gamePanel = Draw()
val titleText = "Hyper Pong"
val guiMode: GuiModes = GuiModes.StartMenu

fun initGame() {
    Gui()
}

fun joinGame(id: String) {
    println("joining game $id")
}