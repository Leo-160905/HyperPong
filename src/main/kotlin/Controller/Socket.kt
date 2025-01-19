package org.example.Controller

import java.net.Socket


//val socket = Socket(IP, PORT)

fun searchGames(): ArrayList<Game> {
    val games = ArrayList<Game>()
    games.add(Game("Leos1", "123"))
    games.add(Game("Leos2", "124"))
    games.add(Game("Leos3", "125"))
    return games
}
