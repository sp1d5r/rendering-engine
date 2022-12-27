package core;

import core.entity.Model;
import core.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
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
    private List<Integer> textures = new ArrayList<>();


    public Model loadMode(String fileName) {
        List<String> lines = Utils.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for(String line: lines){
            String[] tokens = line.split("\\s+");
            switch (tokens[0]){
                case "v":
                    // verticies
                    Vector3f verticesVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVec);
                    break;
                case "vt":
                    // vertex textures
                    Vector2f texturesVec = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(texturesVec);
                    break;
                case "vn":
                    Vector3f normalsVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(normalsVec);
                    // vertex normals
                    break;
                case "f":
                    // faces
                    for(int i=1; i< tokens.length; i++){
                        processFace(tokens[i], faces);
                    }
                    break;
                default:
                    break;
            }
        }

        List<Integer> indices = new ArrayList<>();
        float [] verticiesArr = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices) {
            verticiesArr[i * 3] = pos.x;
            verticiesArr[i * 3 + 1] = pos.y;
            verticiesArr[i * 3 + 2] = pos.z;
            i++;
        }

        float[] textCoorArr = new float[vertices.size() * 2];
        float[] normalsArr = new float[vertices.size() * 3];

        for (Vector3i face : faces) {
            processVertex(face.x, face.y, face.z, textures, normals, indices, textCoorArr, normalsArr);
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loadModel(verticiesArr, textCoorArr, indicesArr);
    }

    private static void processVertex(int pos, int textCoord, int normal, List<Vector2f> textCoordList,
                                      List<Vector3f> normalList, List<Integer> indicesList,
                                      float[] textCoordArr, float[] normalArr) {
        indicesList.add(pos);

        if (textCoord >= 0) {
            Vector2f textCoordVec = textCoordList.get(textCoord);
            textCoordArr[pos*2] = textCoordVec.x;
            textCoordArr[pos*2 + 1] = textCoordVec.y;
        }

        if (normal >= 0) {
            Vector3f normalVec = normalList.get(normal);
            normalArr[pos*3] = normalVec.x;
            normalArr[pos*3 + 1] = normalVec.y;
            normalArr[pos*3 + 2] = normalVec.z;
        }

    }
    private static void processFace(String token, List<Vector3i> faces){
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords = -1, normal = -1;

        pos = Integer.parseInt(lineToken[0]) - 1;

        if (length > 1) {
            String textCoord = lineToken[1];
            coords = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;

            if (length > 2) {
                normal = Integer.parseInt(lineToken[2]) - 1;
            }
        }

        Vector3i facesVec = new Vector3i(pos, coords, normal);
        faces.add(facesVec);
    }
    public Model loadModel(float[] vertices, float[] textureCords, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCords);
        unbind();
        return new Model(id, indices.length); //divide by 3 each vertex has x,y,z
    }

    public int loadTexture(String filename) throws  Exception {
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null)
                throw new Exception("Image File " + filename + " not loaded " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();

        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
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
        for(int texture: textures)
            GL30.glDeleteTextures(texture);

    }
}
