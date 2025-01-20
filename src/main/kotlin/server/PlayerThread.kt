package server

import Game
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList

class PlayerThread(
    private val client: Socket,
    val playerId: String,
    private val cThreads: CopyOnWriteArrayList<Thread>
) : Runnable {
    private lateinit var send: DataOutputStream
    private lateinit var receive: DataInputStream
    private var isRunning = true
    private var currentGame: Game? = null
    override fun run() {
        try {
            while (isRunning) {
                client.tcpNoDelay = true
                send = DataOutputStream(client.getOutputStream())
                receive = DataInputStream(client.getInputStream())

                println("connected to client ready to message")

                val message = JSONObject(receive.readUTF())
                if (currentGame == null) preGame(message)
                else whileGame(message)

            }
        } catch (e: Exception) {
            println(e)
        } finally {

            send.close()
            receive.close()
            client.close()
            cThreads.remove(Thread.currentThread())
            println("thread terminated")
        }
    }

    private fun whileGame(message: JSONObject) {
        if (message.getString("action") == "reportReady") {
            var wasChanged = false
            currentGame!!.player.forEach {
                if (it.playerThreadId == playerId)
                    it.isReady = true
                wasChanged = true
                println("Player ${it.playerName} is ready")
            }

            val response = JSONObject()
            if (wasChanged) response.put("action", "confirmNotReady")
            else response.put("action", "error")
            send.writeUTF(response.toString())
        }

        if (message.getString("action") == "reportNotReady") {
            var wasChanged = false
            currentGame!!.player.forEach {
                if (it.playerThreadId == playerId) {
                    it.isReady = false
                    wasChanged = false
                    println("Player ${it.playerName} is not ready")
                }
                wasChanged = true
            }

            val response = JSONObject()
            if (wasChanged) response.put("action", "confirmNotReady")
            else response.put("action", "error")
            send.writeUTF(response.toString())
        }
    }


    private fun preGame(message: JSONObject) {
        if (message.getString("action") == "createGame") {
            val gameName = message.getString("gameName")
            games.add(GameThread(Game(gameName, (games.size + 1).toString())))

            games[games.size - 1].addPlayer(this, playerId, message.getString("playerName"))
            currentGame = games[games.size - 1].game

            val response = JSONObject()
            response.put("action", "createResponse")
            response.put("isSuccessful", true)
            response.put("players", getPlayers(games[games.size - 1].game))
            send.writeUTF(response.toString())
        }
        if (message.getString("action") == "requestGameList") {
            val obj = JSONObject()
            obj.put("action", "responseGameList")
            val gamesJson = JSONArray()
            for (game in games) {
                val gameObj = JSONObject()
                gameObj.put("name", game.game.name)
                gameObj.put("id", game.game.id)
                gamesJson.put(gameObj)
            }
            obj.put("games", gamesJson)
            println(obj.toString())
            send.writeUTF(obj.toString())
        }
        if (message.getString("action") == "joinGame") {
            val gameId = message.getString("gameId")
            var gameIndex = -1
            games.forEachIndexed { i, it -> if (it.game.id == gameId) gameIndex = i }


            games[gameIndex].addPlayer(this, playerId, message.getString("playerName"))
            currentGame = games[gameIndex].game

            val response = JSONObject()
            response.put("action", "joiningConfirmation")
            response.put("players", getPlayers(games[gameIndex].game))
            response.put("isSuccessful", true)
            send.writeUTF(response.toString())

        }
    }

    private fun getPlayers(game: Game): JSONArray {
        val playersJson = JSONArray()
        println("players: ${game.player.size}, ${game.id}")
        game.player.forEach {
            val obj = JSONObject()
            obj.put("playerName", it.playerName)
            obj.put("playerId", it.playerThreadId)
            obj.put("isPlayerReady", it.isReady)
            playersJson.put(obj)
        }
        return playersJson
    }

    fun updateLobbyList() {
        if(currentGame != null) {
            println("now updating players")
            val message = JSONObject()
            message.put("action", "updateLobby")
            message.put("players", getPlayers(currentGame!!))

            send.writeUTF(message.toString())
        }
    }
}