package game

import engine.Utils
import engine.Window
import engine.graph.Mesh
import engine.graph.ShaderProgram
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

    @Throws(Exception::class)
    fun init(window: Window) {

        // Create shader
        shaderProgram = ShaderProgram()
        shaderProgram!!.createVertexShader(Utils.loadResource("/vertex.glsl"))
        shaderProgram!!.createFragmentShader(Utils.loadResource("/fragment.glsl"))
        shaderProgram!!.link()

        // Create projection matrix
        var aspectRatio = window.width.toFloat() / window.height.toFloat()
        println(aspectRatio)
        projectionMatrix = Matrix4f().perspective(fov, aspectRatio, zNear, zFar)
        shaderProgram!!.createUniform("projectionMatrix")

    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, mesh: Mesh) {
        clear()

        if (window.resized) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }

        shaderProgram!!.bind()

        // Set the uniform with our proj matrix (for rotating, scaling, etc..)
        shaderProgram!!.setUniform("projectionMatrix", projectionMatrix!!)

        // Bind VAO and draw the mesh
        glBindVertexArray(mesh.vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)


        // Restore state
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)

        shaderProgram!!.unbind()
    }

    fun cleanup() {
        if (shaderProgram != null) {
            shaderProgram!!.cleanup()
        }
    }
}