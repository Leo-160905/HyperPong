package server

import Game
import java.net.ServerSocket
import java.util.concurrent.CopyOnWriteArrayList

var port = 0
lateinit var server:ServerSocket
val games = ArrayList<GameThread>()
val cThreads = CopyOnWriteArrayList<Thread>()
var connectedClients = 0;

fun main() {
    games.add(GameThread(Game("game 1", "1")))
    games.add(GameThread(Game("game 2", "2")))
    print("Booting Server \ninput server port: ")
    port = readln().toInt()
    println("starting socket")
    server = ServerSocket(port)
    while (true) {
        println("${cThreads.size} clients are connected")
        println("waiting for client...")
        val socket = server.accept()
        println("connected to client")
        val thread = Thread(PlayerThread(socket, "${ connectedClients + (System.currentTimeMillis() / 1000).toInt()}", cThreads))
        cThreads.add(thread)
        thread.start()
    }
}

//fun closeConnection(id: Int) {
//    var index = -1
//    cThreads.forEachIndexed { i, it -> if(it.clientID == id) index = i }
//    connectedClients--
//    if(index > 0 && index < cThreads.size) {
//        cThreads[index].interrupt()
//        cThreads.remove(cThreads[index])
//    }
//    println("thread removed")
//}