package engine.graph

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import java.nio.FloatBuffer

class ShaderProgram {

    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0
    private val uniforms: MutableMap<String, Int> = HashMap()

    init {
        init()
    }

    @Throws(Exception::class)
    fun init() {
        programId = glCreateProgram()
        if (programId == 0) {
            throw Exception("Could not create Shader")
        }
    }

    @Throws(Exception::class)
    fun createUniform(uniformName: String) {
        var uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw Exception("Could not find uniform $uniformName")
        }
        uniforms.put(uniformName, uniformLocation)
    }

    fun setUniform(uniformName: String, value: Matrix4f) {

        // Dump the matrix into a float buffer
        try {
            var stack: MemoryStack = MemoryStack.stackPush()
            var fb: FloatBuffer = stack.mallocFloat(16)
            value.get(fb)
            glUniformMatrix4fv(uniforms[uniformName]!!, false, fb)
        } catch (e: Exception) {throw Exception("$e \nCould not set uniform")}
    }

    @Throws(Exception::class)
    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    @Throws(Exception::class)
    fun createFragmentShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    @Throws(Exception::class)
    fun createShader (shaderCode: String, shaderType: Int): Int{
        var shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw Exception("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw Exception("Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}")
        }

        glAttachShader(programId, shaderId)

        return shaderId
    }

    @Throws(Exception::class)
    fun link() {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw Exception("Error linking Shader code: ${glGetProgramInfoLog(programId, 1024)}")
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId!!)
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId!!)
        }

        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: ${glGetProgramInfoLog(programId, 1024)}")
        }
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

}