package core;

import core.entity.Entity;
import core.entity.Model;
import core.utils.Transformation;
import core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderManager {

    private final WindowManager window;
    private ShaderManager shader;
    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        // shader.createGeometryShader(Utils.loadResource("/shaders/geometry.gs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();

        // You can only access uniforms after the shader has been linked.
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");


    }

    public void render(Entity entity, Camera camera) {
        clear();
        shader.bind();
//        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
        shader.setUniform("projectionMatrix", window.updateProjectionMatric());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));


        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0); // Vertex Positions
        if (entity.getModel().usesTexture){
            GL20.glEnableVertexAttribArray(1); // Vertex Textures
            GL13.glActiveTexture(GL13.GL_TEXTURE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
            GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            GL20.glDisableVertexAttribArray(1);
        } else {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0,entity.getModel().getVertexCount());
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup();
    }
}
