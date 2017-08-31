package engine

import engine.graph.Mesh
import org.joml.Vector3f

open class Item (var mesh: Mesh) {

    var position = Vector3f(0f, 0f, 0f)

    var scale = 1.0f

    var rotation = Vector3f(0f, 0f, 0f)

    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }

}