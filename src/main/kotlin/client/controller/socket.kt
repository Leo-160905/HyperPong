package client.controller

import Player
import client.domain
import client.playerName
import client.port
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket


lateinit var socket:Socket
lateinit var send: DataOutputStream
lateinit var receive: DataInputStream

fun startRuntimeThread() {
    gameIsRunning = true
    val gameThread = Thread {
        run {
            while (gameIsRunning) {
                if (receive.available() > 0) {
                    val message = JSONObject(receive.readUTF())
                    if (message.get("action") == "updateLobby") {
                        initPlayers(message)
                        gui.gameLobby()
                    }
                }
            }
        }
    }
    gameThread.start()
}

fun connect() {
    socket = Socket(domain, port)
    socket.tcpNoDelay = true
    send = DataOutputStream(socket.getOutputStream())
    receive = DataInputStream(socket.getInputStream())
    println("connected to server")
}

fun searchGames(): ArrayList<DummyGame> {
    println("sending join request")
    val obj = JSONObject()
    obj.put("action", "requestGameList")
    send.writeUTF(obj.toString())

    val games = ArrayList<DummyGame>()
    val input = receive.readUTF()
    println(input)
    val message = JSONObject(input)
    if (message.getString("action") == "responseGameList") {
        val jsonArray = JSONArray(message.getJSONArray("games"))
        for (i in 0 until jsonArray.length()) {
            val gameObj = jsonArray.getJSONObject(i)
            games.add(DummyGame(gameObj.get("name").toString(), gameObj.get("id").toString()))
        }
    }

    return games
}

fun createGameRequest(name:String) {
    val createRequest = JSONObject()
    createRequest.put("action", "createGame")
    createRequest.put("gameName", name)
    createRequest.put("playerName", playerName)
    send.writeUTF(createRequest.toString())

    val response = JSONObject(receive.readUTF())
    if(response.getString("action") == "createResponse") {
        if(response.getBoolean("isSuccessful")) {
            initPlayers(response)
            joinedGame()
        }
    }
}

fun sendJoinRequest(id: String) {
    val createRequest = JSONObject()
    createRequest.put("action", "joinGame")
    createRequest.put("gameId", id)
    createRequest.put("playerName", playerName)
    send.writeUTF(createRequest.toString())

    val response = JSONObject(receive.readUTF())
    if(response.getString("action") == "joiningConfirmation") {
        if(response.getBoolean("isSuccessful")) {
            initPlayers(response)
            joinedGame()
        }
    }
}

fun initPlayers(response: JSONObject) {
    players.clear()
    println("init players")
    println(response.toString())
    response.getJSONArray("players").forEachIndexed { i, it ->
        it as JSONObject
        players.add(Player(it.get("playerId").toString(), it.get("playerName").toString()))
        players[i].isReady = it.getBoolean("isPlayerReady")
    }
}

fun sendReady() {
    isPlayerReady = true
    val readyObj = JSONObject()
    readyObj.put("action", "reportReady")
    send.writeUTF(readyObj.toString())

//    val response = JSONObject(receive.readUTF())
//    println(response.toString())
//    if(response.getString("action") == "confirmReady"){
//        println("success")
//    }
}

fun sendNotReady() {
    isPlayerReady = false
    val readyObj = JSONObject()
    readyObj.put("action", "reportNotReady")
    send.writeUTF(readyObj.toString())

//    val response = JSONObject(receive.readUTF())
//    println(response.toString())
//    if(response.getString("action") == "confirmNotReady"){
//        println("success")
//    }

}

fun closeConnection() {
    send.writeUTF("disconnect")
    send.close()
    receive.close()
    socket.close()
}