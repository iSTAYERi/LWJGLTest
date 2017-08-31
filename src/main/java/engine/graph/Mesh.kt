package engine.graph

import java.nio.FloatBuffer
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.IntBuffer
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW
import org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER
import org.lwjgl.opengl.GL11.GL_FLOAT



class Mesh (positions: FloatArray, colours: FloatArray, indices: IntArray){

    val vaoId: Int
    private val posVboId: Int
    private val colourVboId: Int
    private val idxVboId: Int
    val vertexCount: Int

    init {
        var posBuffer: FloatBuffer? = null
        var colourBuffer: FloatBuffer? = null
        var indicesBuffer: IntBuffer? = null

        try {
            vertexCount = indices.size

            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Position VBO
            posVboId = glGenBuffers()
            posBuffer = MemoryUtil.memAllocFloat(positions.size)
            posBuffer!!.put(positions).flip()
            glBindBuffer(GL_ARRAY_BUFFER, posVboId)
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            //Colour VBO
            colourVboId = glGenBuffers()
            colourBuffer = MemoryUtil.memAllocFloat(colours.size)
            colourBuffer!!.put(colours).flip()
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId)
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

            //Index VBO
            idxVboId = glGenBuffers()
            indicesBuffer = MemoryUtil.memAllocInt(indices.size)
            indicesBuffer.put(indices).flip()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)

        }finally {
            when {
                colourBuffer != null -> MemoryUtil.memFree(colourBuffer)
                posBuffer != null -> MemoryUtil.memFree(posBuffer)
                indicesBuffer != null -> MemoryUtil.memFree(indicesBuffer)
            }
        }
    }

    fun render(){
        // Bind VAO and draw the mesh
        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)


        // Restore state
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)

        //Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(posVboId)
        glDeleteBuffers(colourVboId)
        glDeleteBuffers(idxVboId)

        //Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)

    }

}





