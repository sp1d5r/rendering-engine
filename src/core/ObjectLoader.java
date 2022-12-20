package core;

import core.entity.Model;
import core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    // see here: https://www.youtube.com/watch?v=WMiggUPst-Q&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=2
    // vao - Vertex Attribute Objects
    //  this represents a certain attribute about each vertex i.e. position
    // vbo - Vertex Buffer Objects
    //  this represents the data for the attribute (in vao) about that
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public Model loadModel(float[] vertices, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        unbind();
        return new Model(id, vertices.length/ 3); //divide by 3 each vertex has x,y,z
    }

    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNo, int vertexCount, float[] data){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        // we can unbind the buffer - using 0
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for(int vao: vaos)
            GL30.glDeleteVertexArrays(vao);
        for(int vbo: vbos)
            GL30.glDeleteBuffers(vbo);

    }
}
