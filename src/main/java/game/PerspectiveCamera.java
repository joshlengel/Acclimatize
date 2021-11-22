package game;

import org.joml.Matrix4f;
import window.Window;

public class PerspectiveCamera extends Camera {

    public float fov;
    public float clipNear, clipFar;

    private final Window window;

    public PerspectiveCamera(Window window) {
        fov = (float)(Math.PI * 0.5);
        clipNear = 0.1f;
        clipFar = 100.0f;

        this.window = window;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        float tanHalfFOV = (float)Math.tan(fov * 0.5);

        Matrix4f projection = new Matrix4f();
        projection.m00(1.0f / (tanHalfFOV * window.getAspectRatio()));
        projection.m11(1.0f / tanHalfFOV);
        projection.m22((clipFar + clipNear) / (clipFar - clipNear));
        projection.m23(-2.0f * clipFar * clipNear / (clipFar - clipNear));
        projection.m32(1.0f);
        projection.m33(0.0f);

        return projection;
    }
}
