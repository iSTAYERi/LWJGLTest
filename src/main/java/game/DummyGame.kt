package game

import engine.IGameLogic
import engine.Item
import engine.Window
import game.draws.FVCMesh
import org.lwjgl.glfw.GLFW.*

class DummyGame: IGameLogic {

    var dispXInc = 0
    var dispYInc = 0
    var dispZInc = 0
    var scaleInc = 0
    var renderer: Renderer = Renderer()
    var fvcMesh: FVCMesh? = null
    var items: Array<Item>? = null

    @Throws(Exception::class)
    override fun init(window: Window) {

        renderer.init(window)

        fvcMesh = FVCMesh()
        var fvc = Item(fvcMesh!!.mesh)
        items = arrayOf(fvc)
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
            window.isKeyPressed(GLFW_KEY_PAGE_UP) -> scaleInc = 1
            window.isKeyPressed(GLFW_KEY_PAGE_DOWN) -> scaleInc = -1
        }
    }

    override fun update(interval: Float) {
        for (item: Item in items!!) {
            //Update position
            var itemPos = item.position
            var posX = itemPos.x + dispXInc * 0.02f
            var posY = itemPos.y + dispYInc * 0.02f
            var posZ = itemPos.z + dispZInc * 0.02f
            item.setPosition(posX, posY, posZ)

            //Update scale
            var scale: Float = item.scale
            scale += scaleInc * 0.05f
            if (scale < 0f) {
                scale = 0f
            }
            item.scale = scale

            //Update rotation angle
            var rotationZ: Float = item.rotation.z + 1.5f
            if (rotationZ > 360) {
                rotationZ = 0f
            }
            item.rotation.z = rotationZ
        }
    }

    override fun render(window: Window) {
        renderer.render(window, items!!)
    }

    override fun cleanup() {
        renderer.cleanup()
        fvcMesh!!.mesh.cleanup()
    }
}