package test;

import core.*;
import core.entity.Entity;
import core.entity.Model;
import core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static core.utils.Consts.DIST_TO_OBJECT_Z;

public class TestGame implements ILogic {
    private int direction = 0;
    private float colour = 0.0f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private Entity entity;
    private final WindowManager window;
    private Camera camera;

    private String viewOption = "normal";

    Vector3f cameraInc;

    public TestGame() {
        renderer = new RenderManager();
        window =  Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0,0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadMode("/models/female.obj");
        entity = new Entity(model, new Vector3f(0,-100,-DIST_TO_OBJECT_Z), new Vector3f(0, 0,0), 1);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);

        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_1))
            viewOption = "reset";

        if (window.isKeyPressed(GLFW.GLFW_KEY_2))
            viewOption = "left";

        if (window.isKeyPressed(GLFW.GLFW_KEY_3))
            viewOption = "right";


//        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
//            direction = 1;
//        }
//        else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
//            direction = -1;
//        } else {
//            direction = 0;
//        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if(mouseInput.isRightButtonPress()) {
            Vector2f rotVec = mouseInput.getDisplayVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y* Consts.MOUSE_SENSITIVITY, 0);
        }


        if (viewOption.equals("left")){
            camera.setRotation(0, 90, 0);
            camera.setPosition(-DIST_TO_OBJECT_Z, 0, -DIST_TO_OBJECT_Z);
            viewOption = "normal";
        }

        if (viewOption.equals("right")){
            camera.setRotation(0, -90, 0);
            camera.setPosition(DIST_TO_OBJECT_Z, 0, -DIST_TO_OBJECT_Z);
            viewOption = "normal";
        }

        if (viewOption.equals("reset")) {
            camera.setRotation(0, 0, 0);
            camera.setPosition(0, 0, 0);
            viewOption = "normal";
        }


        // Color information
        colour += direction * 0.01f;
        if (colour > 1)
            colour = 1.0f;
        else if (colour <= 0)
            colour = 0.0f;

//        if (entity.getPos().x < -1.5f)
//            entity.getPos().x = 1.5f;
//        entity.getPos().x -= 0.01f;

        entity.incRotation(0.0f, 0.5f, 0.0f);
    }

    @Override
    public void render() {
        if (window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(colour, colour, colour, 0.0f);
        renderer.render(entity, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
