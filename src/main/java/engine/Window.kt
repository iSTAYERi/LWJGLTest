package engine

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.system.MemoryUtil.NULL
import javax.swing.Spring.height
import javax.swing.Spring.width
import org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback



class Window (var title: String, var width: Int, var height: Int, var vSync: Boolean) {

    var resized = false
    var windowHandle: Long = NULL

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
        if (windowHandle == NULL) {
            throw RuntimeException ("Failed to create the GLFW window")
        }

        glfwSetFramebufferSizeCallback(windowHandle) { window, width, height ->
            this.width = width
            this.height = height
            this.resized = true
        }

        glfwSetKeyCallback(windowHandle){window, key, scancode, action, mods ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true)
            }
        }

        var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        )

        glfwMakeContextCurrent(windowHandle)

        if (vSync){
            glfwSwapInterval(1)
        }

        glfwShowWindow(windowHandle)

        GL.createCapabilities()

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }

    fun setClearColor(r: Float, g: Float, b: Float, alpha: Float){
        glClearColor(r, g, b, alpha)
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS
    }

    fun windowShouldClose(): Boolean{
        return glfwWindowShouldClose(windowHandle)
    }

    fun update() {
        glfwSwapBuffers(windowHandle)
        glfwPollEvents()
    }
}