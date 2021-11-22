package game;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class Camera {

    public final Vector3f position;
    public final Quaternionf rotation;

    public CameraController controller;

    public Camera() {
        position = new Vector3f();
        rotation = new Quaternionf();
    }

    public void setController(CameraController controller) { this.controller = controller; }

    public Matrix4f getViewMatrix() {
        Matrix4f translation = new Matrix4f();
        translation.m03(-position.x);
        translation.m13(-position.y);
        translation.m23(-position.z);

        Matrix4f rotation = new Matrix4f();
        Quaternionf inverted = new Quaternionf();
        this.rotation.invert(inverted).get(rotation);

        return translation.mul(rotation);
    }

    public abstract Matrix4f getProjectionMatrix();
}
