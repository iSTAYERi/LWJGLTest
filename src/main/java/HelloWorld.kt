import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.IntBuffer


class HelloWorld {

    companion object {

        const val SLEEP_TIME_FPS = 1000L/60L

        @JvmStatic fun main(args: Array<String>){
            HelloWorld().run()
        }
    }

    var window = 0L
    var width:IntBuffer? = null
    var height: IntBuffer? = null

    fun run() {
        System.out.println("Hello LWJGL ${Version.getVersion()} !")

        init()
        loop()
        dispose()
    }

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        window = glfwCreateWindow(800, 500, "Hello World!", NULL, NULL)
        if (window == NULL){
            glfwTerminate()
            throw RuntimeException("Failed to create the GLFW window")
        }


        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window) { window, key, scancode, action, mods ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true)
            // Тоже самое, что и ниже
//            if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS && glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS)
//                glfwSetWindowShouldClose(window, true)
            if (key == GLFW_KEY_Q && action == GLFW_RELEASE && mods == GLFW_MOD_CONTROL)
                glfwSetWindowShouldClose(window, true)
        }

        // Get the thread stack and push a new frame
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            )
        } // the stack frame is popped automatically

        width = MemoryUtil.memAllocInt(1)
        height = MemoryUtil.memAllocInt(1)

        // Make the OpenGL context current
        glfwMakeContextCurrent(window)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window)
    }



    fun loop() {

        var timer = Timer()
        var delta = 0f
        var accumulator = 0f
        var interval = 1f / 60f
        var alpha = 0f

        GL.createCapabilities()
        glClearColor(1.0f, 1.0f, 0.5f, 0.0f)

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            delta = timer.getDelta()
            accumulator += delta

            input()

            while (accumulator >= interval) {
//                update()
                timer.updateUPS()
                accumulator -= interval
            }

            alpha = accumulator / interval

            render(alpha)
            timer.updateFPS()
            print("FPS: ${timer.fps}")

            timer.update()

            glfwSwapBuffers(window) // swap the color buffers
            glfwPollEvents()
        }
    }

    fun input(){

    }

    fun update(delta: Float){

    }

    fun render(alpha: Float){

        var ratio = 0f

        /* Get width and height to calcualte the ratio */
        glfwGetFramebufferSize(window, width, height)
        ratio = width!!.get() / height!!.get().toFloat()

        /* Rewind buffers for next get */
        width!!.rewind()
        height!!.rewind()

        /* Set viewport and clear screen */
        glViewport(0, 0, width!!.get(), height!!.get())
        glClear(GL_COLOR_BUFFER_BIT)

        /* Set ortographic projection */
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho((-ratio).toDouble(), ratio.toDouble(), -1.0, 1.0, 1.0, -1.0)
        glMatrixMode(GL_MODELVIEW)

        /* Rotate matrix */
        glLoadIdentity()
        glRotatef(glfwGetTime().toFloat() * alpha, 0f, 0f, 1f)

        /* Render triangle */
        glBegin(GL_TRIANGLES)
        glColor3f(1f, 0f, 0f)
        glVertex3f(-0.6f, -0.4f, 0f)
        glColor3f(0f, 1f, 0f)
        glVertex3f(0.6f, -0.4f, 0f)
        glColor3f(0f, 0f, 1f)
        glVertex3f(0f, 0.6f, 0f)
        glEnd()

        /* Swap buffers and poll Events */
        glfwSwapBuffers(window)
        glfwPollEvents()

        /* Flip buffers for next loop */
        width!!.flip()
        height!!.flip()
    }

    fun dispose(){
        glfwDestroyWindow(window)
        glfwFreeCallbacks(window)

        glfwTerminate()
        glfwSetErrorCallback(null).free()
    }
}