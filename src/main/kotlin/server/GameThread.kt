package server

import Game
import Player

class GameThread(val game: Game) : Runnable {
    private val playerThreadList = ArrayList<PlayerThread>()
    override fun run() {

    }

    fun addPlayer(pThread: PlayerThread, playerId: String, playerName: String) {
        game.addPlayer(playerId, playerName)
        playerThreadList.add(pThread)
        updatePlayerListOnClient()
    }

    fun changePlayerReadyState(playerId: String, isReady: Boolean) {
        game.player.forEach {
            if (it.playerThreadId == playerId) it.isReady = isReady
        }
        updatePlayerListOnClient()
    }

    private fun updatePlayerListOnClient() {
        for (player in playerThreadList) {
            player.updateLobbyList()
        }
    }

    fun removePlayer(playerId: String) {
        var index = -1
        game.player.forEachIndexed { i, it ->
            if(it.playerThreadId == playerId) index = i
        }
        game.player.removeAt(index)
        playerThreadList.removeAt(index)
        updatePlayerListOnClient()
    }
}