package core.entity;

public class Model {
    /* Represents a model stored in memory, can be accessed by it's ID */
    private int id;
    private int vertexCount;

    public Model(int id, int vertexCount){
        this.id = id;
        this.vertexCount = vertexCount;
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

}
