package client.controller

import Player
import client.gui.Draw
import client.gui.Gui
import java.awt.Toolkit
import javax.swing.Timer


val gamePanel = Draw()
const val TITLE_TEXT = "Hyper Pong"
lateinit var gui: Gui
var isLeftPressed = false
var isRightPressed = false
var racketPos = 0
val players = ArrayList<Player>()
var isPlayerReady = false
var gameIsRunning = false

fun initGame(isConfigured: Boolean) {
    gui = Gui(isConfigured)
    if(isConfigured) connect()
}

fun joinGame(id: String) {
    println("joining game $id")
    sendJoinRequest(id)
}

fun joinedGame() {
    println("joined Successfully")
    gui.gameLobby()
    startRuntimeThread()
}

fun updateLobby() {

}

fun startGame() {
    gui.loadGamePanel()

    val t = Timer(40) {
        if(isLeftPressed) {
            racketPos = (racketPos + 10) % 360
            println(racketPos)
        }
        if(isRightPressed) {
            racketPos = (racketPos + 350) % 360
            println(racketPos)
        }

        gamePanel.repaint()
        Toolkit.getDefaultToolkit().sync()
    }
    t.start()
    t.isRepeats = true
}