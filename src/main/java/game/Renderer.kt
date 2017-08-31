package game

import engine.Item
import engine.Utils
import engine.Window
import engine.graph.Mesh
import engine.graph.ShaderProgram
import engine.graph.Transformation
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*


class Renderer {

    private var shaderProgram: ShaderProgram? = null

    private val fov = Math.toRadians(60.0).toFloat()

    private val zNear = 0.01f

    private val zFar = 1000.0f

    private var projectionMatrix: Matrix4f? = null

    var transformation: Transformation = Transformation()

    @Throws(Exception::class)
    fun init(window: Window) {

        // Create shader
        shaderProgram = ShaderProgram()
        shaderProgram!!.createVertexShader(Utils.loadResource("/vertex.glsl"))
        shaderProgram!!.createFragmentShader(Utils.loadResource("/fragment.glsl"))
        shaderProgram!!.link()

        // Create uniforms for matrices
        shaderProgram!!.createUniform("projectionMatrix")
        shaderProgram!!.createUniform("worldMatrix")

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f)

    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, items: Array<Item>) {
        clear()

        if (window.resized) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }

        shaderProgram!!.bind()

        //Update projection Matrix
        var projMatrix = transformation.getProjectionMatrix(
                fov,
                window.width.toFloat(),
                window.height.toFloat(),
                zNear,
                zFar)
        shaderProgram!!.setUniform("projectionMatrix", projMatrix)

        //Render each Item
        for (item: Item in items) {
            var worldMatrix = transformation.getWorldMatrix(
                    item.position,
                    item.rotation,
                    item.scale)
            shaderProgram!!.setUniform("worldMatrix", worldMatrix)
            item.mesh.render()
        }

        shaderProgram!!.unbind()
    }

    fun cleanup() {
        if (shaderProgram != null) {
            shaderProgram!!.cleanup()
        }
    }
}