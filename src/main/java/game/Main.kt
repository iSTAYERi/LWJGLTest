package game

import engine.GameEngine
import engine.IGameLogic
import org.lwjgl.opengl.GL11

fun main(args: Array<String>) {
    try {
        var vSync = true
        var gameLogic: IGameLogic = DummyGame()
        var gameEng = GameEngine("Not a Game", 600, 480, vSync, gameLogic)
        gameEng.start()
    } catch (ex: Exception) {
        ex.printStackTrace()
        System.exit(-1)
    }
}