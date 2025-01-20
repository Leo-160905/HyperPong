import java.awt.Point

class Player(val playerThreadId: String, val playerName: String) {
    var pos: Point = Point()
    var racketRot: Int = 0
    var ballPos: Point = Point()
    var ballRot: Int = 0
    var isReady: Boolean = false
}