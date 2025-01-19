package org.example


import org.example.Controller.initGame
import org.json.JSONObject
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


const val CONFIG_FILE_PATH = "./config.json"
var port: Int = -1
var domain = ""
var playerName = ""

fun main() {
    var isConfigured = true
    if(!Files.exists(Paths.get(CONFIG_FILE_PATH))) {
        isConfigured = false
        val file = File(CONFIG_FILE_PATH)
        file.createNewFile()
    }
    else {
        val file = Files.readAllBytes(Paths.get(CONFIG_FILE_PATH)).toString(Charsets.UTF_8)
        val jsonObject = JSONObject(file)
        playerName = jsonObject.getString("name")
        domain = jsonObject.getString("domain")
        port = jsonObject.getInt("port")
    }

    initGame(isConfigured)
}