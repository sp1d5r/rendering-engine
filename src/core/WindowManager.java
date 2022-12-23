package core;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

public class WindowManager {

    // Window Constants
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.001f;
    public static final float Z_FAR = 1000f;

    private final String title;

    private int width, height;
    private long window;

    private boolean resize, vSync;

    private Matrix4f projectionMatrix;

    /**
     * @param title - title of the screen
     * @param width - Width of the window, Set to 0 to maximize
     * @param height - Height of the window, set to 0 to maximize
     * @param vSync - sets the refresh rate to that of the monitor
     */
    public WindowManager(String title, int width, int height, boolean vSync){

        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;

        this.projectionMatrix = new Matrix4f();
    }

    public void init() {
        // This will send us messages when errors occur
        GLFWErrorCallback.createPrint(System.err).set();

        // initialise LWJGL
        if (!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialise GLFW");

        // set some defaults for the window
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        boolean maximised = false;
        if (width==0 || height ==0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximised = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL){
            throw new RuntimeException("failed to create the GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        if (maximised) {
            GLFW.glfwMaximizeWindow(window);
        } else {
            /* This primary monitor stuff will be customised to each screen*/
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) /2, (vidMode.height() - height) / 2);
        }

        // set current context to window.
        GLFW.glfwMakeContextCurrent(window);

        // hanlde vSync
        if(isvSync()) {
            GLFW.glfwSwapInterval(1);
        }

        // Now finally show the window
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        // Setting some additional functionality to open GL - check docs because i will forget
        GL11.glClearColor(0f, 0f, 0f, 0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);

        // This will break rendering
        //GL11.glEnable(GL11.GL_CULL_FACE);
        //GL11.glEnable(GL11.GL_BACK);

    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        // Poll Events will tell GL to render all the objects we've placed in a queue
        GLFW.glfwPollEvents();
    }

    public void cleanup(){
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a){
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode){
        // Checks to see if the key is pressed
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        // checks if we should close the window for infinite loops
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public boolean isResize() {
        return resize;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public Matrix4f updateProjectionMatric() {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatric(Matrix4f matrix, int width, int height) {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
