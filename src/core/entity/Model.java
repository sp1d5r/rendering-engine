package core.entity;

public class Model {
    /* Represents a model stored in memory, can be accessed by it's ID */
    private int id;
    private int vertexCount;
    private Texture texture;
    public boolean usesTexture;

    public Model(int id, int vertexCount){
        this.id = id;
        this.vertexCount = vertexCount;
        this.usesTexture = false;
    }

    public Model(int id, int vertexCount, Texture texture){
        this.id = id;
        this.vertexCount = vertexCount;
        this.texture = texture;
        this.usesTexture = true;
    }

    public Model(Model model, Texture texture){
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.texture = texture;
        this.usesTexture = true;
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.usesTexture = true;
        this.texture = texture;
    }
}
