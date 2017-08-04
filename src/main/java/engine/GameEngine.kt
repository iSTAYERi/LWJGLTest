package engine

class GameEngine(windowTitle: String,
                 width: Int,
                 height: Int,
                 vSync: Boolean,
                 var gameLogic: IGameLogic) : Runnable {

    var gameLoopThread = Thread(this, "GAME_LOOP_THREAD")
    var window = Window(windowTitle, width, height, vSync)
//    Must to create class Timer
    var timer = Timer()
}

