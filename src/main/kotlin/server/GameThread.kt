package server

import Game

class GameThread(val game: Game) : Runnable {
    private val playerThreadList = ArrayList<PlayerThread>()
    override fun run() {

    }

    fun addPlayer(pThread: PlayerThread, playerId: String, playerName: String) {
        game.addPlayer(playerId, playerName)
        playerThreadList.add(pThread)
        if(game.player.size > 1) {
            for (player in playerThreadList) {
                player.updateLobbyList()
            }
        }
    }
}