package org.example.GUI

import org.example.Controller.gamePanel
import org.example.Controller.joinGame
import org.example.Controller.searchGames
import org.example.Controller.titleText
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.WindowConstants
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.system.exitProcess

class Gui : JFrame() {
//    private val fSize = Toolkit.getDefaultToolkit().screenSize
        private val fSize = Dimension(800,600)
    val cp = contentPane
    val contentPanel = JPanel()

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isUndecorated = true

        contentPanel.preferredSize = fSize
        contentPanel.background = Color.black
        contentPanel.layout = null

        loadStartMenu()

        isVisible = true
        pack()

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                super.keyPressed(e)
                if (e?.keyCode == KeyEvent.VK_ESCAPE) exitProcess(0)
                if (e?.keyCode == KeyEvent.VK_SPACE) loadGamePanel()
            }
        })
    }

    private fun loadStartMenu() {
        clearScreen()
        loadTitle()

        // Menu
        val joinBtn = JButton("Join")
        joinBtn.setBounds((fSize.width - 200) / 2, (fSize.height - 50) / 2, 200, 50)
        joinBtn.isFocusable = false
        joinBtn.isFocusPainted = false
        joinBtn.background = null
        joinBtn.foreground = Color.yellow
        joinBtn.border = BorderFactory.createLineBorder(Color.yellow)
        joinBtn.addActionListener { searchGame() }
        contentPanel.add(joinBtn)

        val createBtn = JButton("Create Game")
        createBtn.setBounds((fSize.width - 200) / 2, (fSize.height + 100) / 2, 200, 50)
        createBtn.isFocusable = false
        createBtn.isFocusPainted = false
        createBtn.background = null
        createBtn.foreground = Color.yellow
        createBtn.border = BorderFactory.createLineBorder(Color.yellow)
        createBtn.addActionListener { createGame() }
        contentPanel.add(createBtn)

        cp.add(contentPanel)
    }

    private fun searchGame() {
        clearScreen()
        loadTitle()

        val games = searchGames()

        val gameBtnList: ArrayList<JButton> = ArrayList()
        for (game in games) {
            val i = games.indexOf(game)
            gameBtnList.add(JButton(game.name))
            gameBtnList[i].setBounds((fSize.width - 200) / 2, (fSize.height + i * 120) / 2, 200, 50)
            gameBtnList[i].isFocusable = false
            gameBtnList[i].isFocusPainted = false
            gameBtnList[i].background = null
            gameBtnList[i].foreground = Color.yellow
            gameBtnList[i].border = BorderFactory.createLineBorder(Color.yellow)
            gameBtnList[i].addActionListener { joinGame(game.id) }
            contentPanel.add(gameBtnList[i])
        }
        loadReturnBtn()

    }

    fun createGame() {
        clearScreen()
        loadTitle()

        loadReturnBtn()
    }

    fun clearScreen() {
        contentPanel.removeAll()
        contentPanel.revalidate()
        contentPanel.repaint()
    }

    private fun loadReturnBtn() {
        val returnBtn = JButton("return")
        returnBtn.setBounds((fSize.width - 200) / 2, (fSize.height) / 3, 200, 50)
        returnBtn.isFocusable = false
        returnBtn.isFocusPainted = false
        returnBtn.background = null
        returnBtn.foreground = Color.yellow
        returnBtn.border = BorderFactory.createLineBorder(Color.white)
        returnBtn.addActionListener { loadStartMenu() }
        contentPanel.add(returnBtn)
    }

    private fun loadTitle() {
        val titleArea = JTextPane()

        val doc: StyledDocument = titleArea.styledDocument
        val center = SimpleAttributeSet()
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER)
        doc.setParagraphAttributes(0, doc.length, center, false)

        titleArea.background = null
        titleArea.isFocusable = false
        titleArea.foreground = Color.blue
        titleArea.isEditable = false
        titleArea.text = titleText
        titleArea.setBounds(0, 20, fSize.width, fSize.height / 5)
        titleArea.setFont(Font("Arial", Font.BOLD, fSize.height / 7))
        contentPanel.add(titleArea)
    }

    fun loadGamePanel() {
        clearScreen()
        gamePanel.preferredSize = fSize
        gamePanel.setBounds(0, 0, fSize.width, fSize.height)
        contentPanel.add(gamePanel)
    }
}