package engine


class GameEngine @Throws(Exception::class) constructor(
        windowTitle: String,
        width: Int,
        height: Int,
        vSync: Boolean,
        var gameLogic: IGameLogic): Runnable{

    companion object {
        const val TARGET_FPS = 75
        const val TARGET_UPS = 30
    }

    var gameLoopThread = Thread(this, "GAME_LOOP_THREAD")
    var window = Window(windowTitle, width, height, vSync)
    var timer = Timer()

    fun start() {
//        var osName = System.getProperty("os.name")
//        if (osName.contains("Mac")) {
//            gameLoopThread.run()
//        } else {
//            gameLoopThread.start()
//        }
        gameLoopThread.start()
    }

    override fun run() {
        try {
            init()
            gameLoop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cleanup()
        }
    }

    @Throws(Exception::class)
    protected fun init() {
        window.init()
        timer.init()
        gameLogic.init(window)
    }

    protected fun gameLoop() {
        var elapsedTime: Float
        var accumulator = 0f
        var interval = 1f / TARGET_UPS

        var running = true
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime()
            accumulator += elapsedTime

            input()

            while (accumulator >= interval) {
                update(interval)
                accumulator -= interval
            }

            render()

            if (!window.vSync){
                sync()
            }
        }

    }

    fun cleanup() {
        gameLogic.cleanup()
    }

//    Если отключена вертикальная синхронизация
    fun sync() {
        var loopSlop = 1f / TARGET_FPS
        var endTime = timer.lastLoopTime + loopSlop
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1)
            }catch (ie: InterruptedException){

            }
        }
    }

    fun input() {
        gameLogic.input(window)
    }

    fun update(interval: Float) {
        gameLogic.update(interval)
    }

    fun render() {
        gameLogic.render(window)
        window.update()
    }
}

