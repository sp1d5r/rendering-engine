package core;

import core.utils.Consts;
import test.TestGame;

/*
* NOTES
*
* The Launcher will handle launching all the different
* screens

----- Notes for multiple windows -----
First, you must create every GLFW window on the "main" thread.
You must also always invoke glfwWaitEvents/glfwPollEvents in the "main" thread.
Other than that you are free to create any number of windows with their own render context threads.
GLFW.glfwMakeContextCurrent(window) makes the GLFW-managed OpenGL context current in the calling thread.
GL.createCapabilities() lets LWJGL know about a context being current in the calling thread and lets LWJGL initialize itself for OpenGL.
You can also once create GL.createCapabilities() in any OpenGL context thread and then reuse the same returned GLCapabilities object in each other rendering thread via GL.setCapabilities(GLCapabilities).

* */


public class Launcher {

    private static WindowManager window;

    private static TestGame game;
    public static void main(String[] args) {
        // go to configuration -> VM Options -> VM Arguments ->  -XstartOnFirstThread

        window = new WindowManager(Consts.FRONT_TITLE, 450, 900, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow(){
        return window;
    }

    public static TestGame getGame() {
        return game;
    }

}
