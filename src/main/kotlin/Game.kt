import org.json.JSONArray
import org.json.JSONObject
import java.awt.Point

class Game(val name: String, val id: String) {
    val player: ArrayList<Player> = ArrayList()

    fun updatePlayer(playerJson: JSONArray) {
        for (i in 0 until playerJson.length()) {
            val jsonObject = JSONObject(playerJson[i].toString())
            player[i].racketRot = jsonObject.getInt("racketRot")
            player[i].ballPos = Point(jsonObject.getInt("ballX"), jsonObject.getInt("ballY"))
            player[i].ballRot = jsonObject.getInt("ballRot")
        }
    }

    fun addPlayer(playerThreadId: String, playerName: String) {
        player.add(Player(playerThreadId, playerName))
        println("$playerName joined the game")
    }
}