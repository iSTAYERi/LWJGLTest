package game

import engine.IGameLogic
import engine.Item
import engine.Window
import game.draws.FVCMesh
import org.lwjgl.glfw.GLFW.*

class ShadowGame: IGameLogic {

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
        fvc.setPosition(-1F, 0F, -4F)
        var fvc2 = Item(fvcMesh!!.mesh)
        fvc2.setPosition(1F, 0F, -4F)
        var fvc3 = Item(fvcMesh!!.mesh)
        fvc3.setPosition(-1F, 0F, -8F)
        var fvc4 = Item(fvcMesh!!.mesh)
        fvc4.setPosition(1F, 0F, -8F)

        items = arrayOf(fvc, fvc2, fvc3, fvc4)
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
//            item.setRotation(rotation, rotation, rotation)
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