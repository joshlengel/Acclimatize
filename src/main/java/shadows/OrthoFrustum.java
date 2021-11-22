package shadows;

import game.PerspectiveCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class OrthoFrustum {

    private static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);

    private final Matrix4f view = new Matrix4f();
    private final Matrix4f projection = new Matrix4f();

    public void update(Vector3f direction, PerspectiveCamera camera) {
        Vector3f right = new Vector3f(UP).cross(direction).normalize();
        Vector3f up = new Vector3f(direction).cross(right);

        view.identity();
        view.m00(right.x);
        view.m01(right.y);
        view.m02(right.z);
        view.m10(up.x);
        view.m11(up.y);
        view.m12(up.z);
        view.m20(direction.x);
        view.m21(direction.y);
        view.m22(direction.z);

        Matrix4f camView = camera.getViewMatrix();
        Matrix4f camProj = camera.getProjectionMatrix();

        float xScale = 1.0f / camProj.m00();
        float yScale = 1.0f / camProj.m11();

        Vector4f[] frustumPoints = new Vector4f[] {
                new Vector4f( camera.clipNear * xScale,  camera.clipNear * yScale, camera.clipNear, 1.0f),
                new Vector4f(-camera.clipNear * xScale,  camera.clipNear * yScale, camera.clipNear, 1.0f),
                new Vector4f( camera.clipNear * xScale, -camera.clipNear * yScale, camera.clipNear, 1.0f),
                new Vector4f(-camera.clipNear * xScale, -camera.clipNear * yScale, camera.clipNear, 1.0f),
                new Vector4f( camera.clipFar * xScale,   camera.clipFar * yScale,  camera.clipFar,  1.0f),
                new Vector4f(-camera.clipFar * xScale,   camera.clipFar * yScale,  camera.clipFar,  1.0f),
                new Vector4f( camera.clipFar * xScale,  -camera.clipFar * yScale,  camera.clipFar,  1.0f),
                new Vector4f(-camera.clipFar * xScale,  -camera.clipFar * yScale,  camera.clipFar,  1.0f)
        };

        for (Vector4f v : frustumPoints) {
            // Move points from camera space to world space
            v.mul(camera.getViewMatrix().invert());

            // Move points from world space to camera space of sun
            v.mul(view);
        }

        float xMin = Float.POSITIVE_INFINITY, xMax = Float.NEGATIVE_INFINITY;
        float yMin = Float.POSITIVE_INFINITY, yMax = Float.NEGATIVE_INFINITY;
        float zMin = Float.POSITIVE_INFINITY, zMax = Float.NEGATIVE_INFINITY;

        // Find ortho frustum boundaries
        for (Vector4f v : frustumPoints) {
            if (v.x < xMin) xMin = v.x;
            else if (v.x > xMax) xMax = v.x;

            if (v.y < yMin) yMin = v.y;
            else if (v.y > yMax) yMax = v.y;

            if (v.z < zMin) zMin = v.z;
            else if (v.z > zMax) zMax = v.z;
        }

        projection.ortho(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    public Matrix4f getViewMatrix() { return view; }
    public Matrix4f getProjectionMatrix() { return projection; }
}
