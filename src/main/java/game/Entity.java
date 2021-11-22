package game;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import render.Shader;

public class Entity {

    public final Vector3f position;
    public final Quaternionf rotation;

    private final Renderable renderable;

    public Entity(Renderable renderable) {
        position = new Vector3f();
        rotation = new Quaternionf();

        this.renderable = renderable;
    }

    public void render(Shader shader) {
        shader.setUniform("model", getModelMatrix());
        renderable.render();
    }

    public Matrix4f getModelMatrix() {
        Matrix4f translation = new Matrix4f();
        translation.m03(position.x);
        translation.m13(position.y);
        translation.m23(position.z);

        Matrix4f rotation = new Matrix4f();
        this.rotation.get(rotation);

        return rotation.mul(translation);
    }
}
