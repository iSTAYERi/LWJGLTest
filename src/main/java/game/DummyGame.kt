package game

import engine.IGameLogic
import engine.Item
import engine.Window
import game.draws.FVCMesh
import org.lwjgl.glfw.GLFW.*
import com.sun.awt.SecurityWarning.setPosition
import engine.graph.Mesh


class DummyGame: IGameLogic {

    var dispXInc = 0
    var dispYInc = 0
    var dispZInc = 0
    var scaleInc = 0
    var renderer: Renderer = Renderer()
    var items: Array<Item>? = null

    @Throws(Exception::class)
    override fun init(window: Window) {

        renderer.init(window)

        val positions = floatArrayOf(
                // VO
                -0.5f,  0.5f,  0.5f,
                // V1
                -0.5f, -0.5f,  0.5f,
                // V2
                0.5f, -0.5f,  0.5f,
                // V3
                0.5f,  0.5f,  0.5f,
                // V4
                -0.5f,  0.5f, -0.5f,
                // V5
                0.5f,  0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f)
        val colours = floatArrayOf(
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f)
        val indices = intArrayOf(
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5
        )
        val mesh = Mesh(positions, colours, indices)
        val gameItem = Item(mesh)
        gameItem.setPosition(0f, 0f, -2f)
        items = arrayOf(gameItem)
    }

    override fun input(window: Window) {
        dispXInc = 0
        dispYInc = 0
        dispZInc = 0
        scaleInc = 0
        when {
            window.isKeyPressed(GLFW_KEY_UP) -> dispYInc = 1
            window.isKeyPressed(GLFW_KEY_DOWN) -> dispYInc = -1
            window.isKeyPressed(GLFW_KEY_RIGHT) -> dispXInc = 1
            window.isKeyPressed(GLFW_KEY_LEFT) -> dispXInc = -1
            window.isKeyPressed(GLFW_KEY_Q) -> dispZInc = 1
            window.isKeyPressed(GLFW_KEY_A) -> dispZInc = -1
            window.isKeyPressed(GLFW_KEY_Z) -> scaleInc = 1
            window.isKeyPressed(GLFW_KEY_X) -> scaleInc = -1
        }
    }

    override fun update(interval: Float) {
        for (item: Item in items!!) {
            //Update position
            var itemPos = item.position
            var posX = itemPos.x + dispXInc * 0.02F
            var posY = itemPos.y + dispYInc * 0.02F
            var posZ = itemPos.z + dispZInc * 0.02F
            item.setPosition(posX, posY, posZ)

            //Update scale
            var scale: Float = item.scale
            scale += scaleInc * 0.05F
            if (scale < 0F) {
                scale = 0F
            }
            item.scale = scale

            //Update rotation angle
            var rotation: Float = item.rotation.z + 1.5f
            if (rotation > 360) {
                rotation = 0f
            }
            item.setRotation(rotation, rotation, rotation)
        }
    }

    override fun render(window: Window) {
        renderer.render(window, items!!)
    }

    override fun cleanup() {
        renderer.cleanup()
        for (item: Item in items!!) {
            item.mesh.cleanup()
        }
    }
}