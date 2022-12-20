package core;

import core.utils.Consts;

/*
* NOTES
*
* The Launcher will handle launching all the different
* screens
*
* */


public class Launcher {

    private static WindowManager window;
    public static EngineManager engine;
    public static void main(String[] args) {
        // go to configuration -> VM Options -> VM Arguments ->  -XstartOnFirstThread

        window = new WindowManager(Consts.FRONT_TITLE, 450, 900, false);
        engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow(){
        return window;
    }
}
