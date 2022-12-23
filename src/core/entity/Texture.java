package core.entity;

public class Texture {
    /* The shaders haven't been adjusted to use textures just yet*/
    private final int id;

    public Texture(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
