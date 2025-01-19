package org.example.GUI

import org.example.CONFIG_FILE_PATH
import org.example.Controller.gamePanel
import org.example.Controller.joinGame
import org.example.Controller.searchGames
import org.example.Controller.TITLE_TEXT
import org.example.domain
import org.example.playerName
import org.example.port
import org.json.JSONObject
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileOutputStream
import javax.swing.*
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.system.exitProcess

class Gui(isConfigured: Boolean) : JFrame() {
    private val fSize = Toolkit.getDefaultToolkit().screenSize
//        private val fSize = Dimension(800,600)
    private val cp: Container = contentPane
    private val contentPanel = JPanel()

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isUndecorated = true
        val ic = ImageIcon(javaClass.getResource("/logo.png"))
        iconImage = ic.image

        contentPanel.preferredSize = fSize
        contentPanel.background = Color.black
        contentPanel.layout = null

        if(isConfigured) loadStartMenu()
        else configGame()


        cp.add(contentPanel)
        isVisible = true
        pack()

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                super.keyPressed(e)
                if (e?.keyCode == KeyEvent.VK_ESCAPE) exitProcess(0)
                if (e?.keyCode == KeyEvent.VK_BACK_SPACE) loadStartMenu()
            }
        })
    }

    private fun loadStartMenu() {
        clearScreen()
        loadTitle()
        requestFocus()

        // Menu
        val joinBtn = getBtnSettings("join", Color.yellow)
        joinBtn.setBounds((fSize.width - 200) / 2, (fSize.height - 50) / 2, 200, 50)

        joinBtn.addActionListener { searchGame() }
        contentPanel.add(joinBtn)

        val createBtn = getBtnSettings("create", Color.yellow)
        createBtn.setBounds((fSize.width - 200) / 2, (fSize.height + 100) / 2, 200, 50)
        createBtn.addActionListener { createGame() }
        contentPanel.add(createBtn)

        val settingsBtn = getBtnSettings("settings", Color.yellow)
        settingsBtn.setBounds((fSize.width - 200) / 2, (fSize.height + 250) / 2, 200, 50)
        settingsBtn.addActionListener { configGame(false) }
        contentPanel.add(settingsBtn)
    }

    private fun configGame(isInitial: Boolean = true) {
        clearScreen()
        loadTitle()
        // name init
        val nameTextField = getTextFieldSettings(Color.yellow)
        nameTextField.setBounds((fSize.width - 200) / 2, (fSize.height - 50) / 2, 200, 50)
        contentPanel.add(nameTextField)

        val namePane = getTextPane("name:", Color.yellow, 25, Dimension(200,50), StyleConstants.ALIGN_RIGHT)
        namePane.location = Point((fSize.width - 620) / 2, (fSize.height + 6 - 50) / 2)
        contentPanel.add(namePane)
        
        // server init
        val domainField = getTextFieldSettings(Color.yellow)
        domainField.setBounds((fSize.width - 200) / 2, (fSize.height + 100) / 2, 200, 50)
        contentPanel.add(domainField)

        val domainPane = getTextPane("game server:", Color.yellow, 25, Dimension(200,50), StyleConstants.ALIGN_RIGHT)
        domainPane.location = Point((fSize.width - 620) / 2, (fSize.height + 6 + 100) / 2)
        contentPanel.add(domainPane)

        val portField = getTextFieldSettings(Color.yellow)
        portField.setBounds((fSize.width - 200) / 2, (fSize.height + 250) / 2, 200, 50)
        contentPanel.add(portField)

        val portPane = getTextPane("port:", Color.yellow, 25, Dimension(200,50), StyleConstants.ALIGN_RIGHT)
        portPane.location = Point((fSize.width - 620) / 2, (fSize.height + 6 + 250) / 2)
        contentPanel.add(portPane)

        val submitBtn = getBtnSettings("submit", Color.yellow)
        submitBtn.setBounds((fSize.width - 200) / 2, (fSize.height + 400) / 2, 200, 50)
        submitBtn.border = BorderFactory.createLineBorder(Color.yellow)
        submitBtn.addActionListener {
            val file = File(CONFIG_FILE_PATH)
            val fos = FileOutputStream(file)
            val jsonObject = JSONObject()

            playerName = nameTextField.text
            domain = domainField.text
            port = portField.text.toInt()
            jsonObject.put("name", playerName)
            jsonObject.put("domain", domain)
            jsonObject.put("port", port)

            fos.write(jsonObject.toString().toByteArray())
            fos.close()

            loadStartMenu()
        }
        contentPanel.add(submitBtn)


        if(!isInitial) {
            loadReturnBtn()
            nameTextField.text = playerName
            domainField.text = domain
            portField.text = port.toString()

        }
    }

    private fun searchGame() {
        clearScreen()
        loadTitle()
        loadReturnBtn()

        val games = searchGames()

        val gameBtnList: ArrayList<JButton> = ArrayList()
        for (game in games) {
            val i = games.indexOf(game)
            gameBtnList.add(getBtnSettings(game.name, Color.yellow))
            gameBtnList[i].setBounds((fSize.width - 200) / 2, (fSize.height - 50 + i * 150) / 2, 200, 50)
            gameBtnList[i].addActionListener { joinGame(game.id) }
            contentPanel.add(gameBtnList[i])
        }

    }

    private fun createGame() {
        clearScreen()
        loadTitle()
        loadReturnBtn()

        val gameNameField = getTextFieldSettings(Color.yellow)
        gameNameField.setBounds((fSize.width - 200) / 2, (fSize.height - 50) / 2, 200, 50)
        contentPanel.add(gameNameField)

        val gameNamePane = getTextPane("Game name:", Color.yellow, 25, Dimension(200,50), StyleConstants.ALIGN_RIGHT)
        gameNamePane.location = Point((fSize.width - 620) / 2, (fSize.height + 6 - 50) / 2)
        contentPanel.add(gameNamePane)

        val createBtn = getBtnSettings("create", Color.yellow)
        createBtn.setBounds((fSize.width - 200) / 2, (fSize.height + 100) / 2, 200, 50)
        contentPanel.add(createBtn)
    }

    private fun clearScreen() {
        contentPanel.removeAll()
        contentPanel.revalidate()
        contentPanel.repaint()
    }

    private fun loadReturnBtn() {
        val returnBtn = getBtnSettings("return", Color.yellow)
        returnBtn.setBounds((fSize.width - 200) / 2, (fSize.height) / 3, 200, 50)
        returnBtn.border = BorderFactory.createLineBorder(Color.white)
        returnBtn.addActionListener { loadStartMenu() }
        contentPanel.add(returnBtn)
    }

    private fun loadTitle() {
        val titleArea = getTextPane(TITLE_TEXT,Color.blue, fSize.height / 7, Dimension(fSize.width, fSize.height / 5), StyleConstants.ALIGN_CENTER)
        titleArea.location = Point(0,0)
        contentPanel.add(titleArea)
    }

    private fun getTextPane(text: String, textColor: Color, fontSize: Int, paneDimension: Dimension, pos: Int): JTextPane {
        val pane = JTextPane()

        val doc: StyledDocument = pane.styledDocument
        val center = SimpleAttributeSet()
        StyleConstants.setAlignment(center, pos)
        doc.setParagraphAttributes(0, doc.length, center, false)

        pane.background = null
        pane.isFocusable = false
        pane.foreground = textColor
        pane.isEditable = false
        pane.text = text
        pane.setBounds(0, 20, paneDimension.width, paneDimension.height)
        pane.setFont(Font(Font.SANS_SERIF, Font.BOLD, fontSize))

        return pane
    }

    private fun getBtnSettings(name: String, color: Color): JButton {
        val btn = JButton(name)
        btn.isFocusable = false
        btn.isFocusPainted = false
        btn.background = null
        btn.foreground = Color.yellow
        btn.border = BorderFactory.createLineBorder(color)
        btn.font = Font(Font.SANS_SERIF, Font.BOLD, 15)
        return btn
    }

    private fun getTextFieldSettings(color: Color): JTextField {
        val tField = JTextField()
        tField.background = null
        tField.foreground = Color.yellow
        tField.font = Font(Font.SANS_SERIF, Font.BOLD, 25)
        tField.border = BorderFactory.createLineBorder(color)
        return tField
    }

    fun loadGamePanel() {
        clearScreen()
        gamePanel.preferredSize = fSize
        gamePanel.setBounds(0, 0, fSize.width, fSize.height)
        contentPanel.add(gamePanel)
    }
}