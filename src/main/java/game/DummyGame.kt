package game

import engine.IGameLogic
import engine.Window
import engine.graph.Mesh
import game.draws.FVC
import org.lwjgl.glfw.GLFW

class DummyGame: IGameLogic {

    var direction = 0
    var color = 0.0f
    var renderer: Renderer = Renderer()
    var mesh: Mesh? = null

    @Throws(Exception::class)
    override fun init(window: Window) {

        renderer.init(window)

        var fvc = FVC()
        var positions = fvc.positions
        var indices = fvc.indices
        var colours = fvc.colours
        mesh = fvc.mesh
    }

    override fun input(window: Window) {
        direction = when {
            window.isKeyPressed(GLFW.GLFW_KEY_UP) -> 1
            window.isKeyPressed(GLFW.GLFW_KEY_DOWN) -> -1
            else -> 0
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
        renderer.render(window, mesh!!)
    }

    override fun cleanup() {
        renderer.cleanup()
        mesh!!.cleanup()
    }
}