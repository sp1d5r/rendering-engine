package core;

public class Launcher {
    public static void main(String[] args) {
        // go to configuration -> VM Options -> VM Arguments ->  -XstartOnFirstThread

        WindowManager window = new WindowManager("Sophia Screen Front", 450, 900, false);
        window.init();

        while(!window.windowShouldClose()) {
            window.update();
        }

        window.cleanup();
    }
}
