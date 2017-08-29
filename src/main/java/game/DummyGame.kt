package game

import engine.IGameLogic
import engine.Window
import engine.graph.Mesh
import org.lwjgl.glfw.GLFW

class DummyGame: IGameLogic {

    var direction = 0
    var color = 0.0f
    var renderer: Renderer = Renderer()
    var mesh: Mesh? = null

    @Throws(Exception::class)
    override fun init() {

        renderer.init()

        var positions: FloatArray = floatArrayOf(
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
        )

        var indices = intArrayOf(0, 1, 3, 3, 1, 2)

        var colours = floatArrayOf(
                0.5f, 0.0f, 0.0f,
                0.0f,0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        )
        mesh = Mesh(positions, colours, indices)
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