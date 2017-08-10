package engine

class Timer {

    var lastLoopTime = 0.0

    fun init() {
        lastLoopTime = getTime()
    }

    fun getTime(): Double {
        return System.nanoTime() / 1000_000_000.0
    }

    fun getElapsedTime(): Float {
        var time = getTime()
        var elapsedTime = (time - lastLoopTime).toFloat()
        lastLoopTime = time
        return elapsedTime
    }

}