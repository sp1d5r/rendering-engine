package core;

import core.utils.Consts;
import test.TestGame;

/*
* NOTES
*
* The Launcher will handle launching all the different
* screens
*
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
