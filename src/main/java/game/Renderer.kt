package game

import engine.Utils
import engine.Window
import engine.graph.Mesh
import engine.graph.ShaderProgram
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*

class Renderer {

    private var shaderProgram: ShaderProgram? = null

    @Throws(Exception::class)
    fun init() {

        shaderProgram = ShaderProgram()
        shaderProgram!!.createVertexShader(Utils.loadResource("/vertex.glsl"))
        shaderProgram!!.createFragmentShader(Utils.loadResource("/fragment.glsl"))
        shaderProgram!!.link()

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