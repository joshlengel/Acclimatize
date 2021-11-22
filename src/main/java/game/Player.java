package game;

import org.joml.Vector3f;
import window.KeyCode;
import window.Window;

public class Player implements CameraController {

    private static final float GRAVITY = -20.0f;

    private final Window window;

    public final float height = 2.0f;
    public final float camHeight = 1.5f;

    public float speed;
    public float jumpSpeed;
    public float sensitivity;

    private float pitch, yaw;

    public Vector3f position, velocity;

    public Player(Window window) {
        this.speed = 2.0f;
        this.jumpSpeed = 10.0f;
        this.sensitivity = 0.3f;

        this.position = new Vector3f();
        this.velocity = new Vector3f();

        this.window = window;
    }

    private float clamp(float x, float y0, float y1) {
        return Math.max(Math.min(x, y1), y0);
    }

    @Override
    public void updateCamera(Camera camera) {
        camera.position.set(position).add(0.0f, camHeight, 0.0f);
        camera.rotation.setAngleAxis(pitch, 1.0f, 0.0f, 0.0f).rotateAxis(yaw, 0.0f, -1.0f, 0.0f);
    }

    public void update(float dt) {
        float siny = (float)Math.sin(yaw);
        float cosy = (float)Math.cos(yaw);

        float vx = 0.0f;
        float vz = 0.0f;

        if (window.keyDown(KeyCode.KEY_UP)) {
            vx += siny * speed;
            vz += cosy * speed;
        }

        if (window.keyDown(KeyCode.KEY_DOWN)) {
            vx -= siny * speed;
            vz -= cosy * speed;
        }

        if (window.keyDown(KeyCode.KEY_LEFT)) {
            vx -= cosy * speed;
            vz += siny * speed;
        }

        if (window.keyDown(KeyCode.KEY_RIGHT)) {
            vx += cosy * speed;
            vz -= siny * speed;
        }

        if (window.keyDown(KeyCode.KEY_SPACE)) {
            velocity.y = jumpSpeed;
        }

        /*if (window.keyDown(KeyCode.KEY_SHIFT)) {
            velocity.y -= speed;
        }*/

        velocity.x = vx;
        velocity.z = vz;

        velocity.y += GRAVITY * dt;
        position.add(new Vector3f(velocity).mul(dt));

        pitch += window.getCursorDY() * sensitivity;
        yaw += window.getCursorDX() * sensitivity;

        pitch = clamp(pitch, (float)(-Math.PI * 0.5), (float)(Math.PI * 0.5));
    }
}
