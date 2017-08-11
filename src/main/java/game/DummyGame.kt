package game

import engine.IGameLogic
import engine.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

class DummyGame: IGameLogic {

    var direction = 0
    var color = 0.0f
    var renderer = Renderer()

    override fun init() {
        renderer.init()
    }

    override fun input(window: Window) {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)){
            direction = 1
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)){
            direction = -1
        } else {
            direction = 0
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.02f
        if (color > 1) {
            color = 1.0f
        } else if (color < 0) {
            color = 0.0f
        }
    }

    override fun render(window: Window) {
        window.setClearColor(color, color, color, 0.0f)
        renderer.render()
    }

    override fun cleanup() {
        renderer.cleanup()
    }
}