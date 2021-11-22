package shadows;

import core.Utils;
import game.PerspectiveCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import render.*;
import window.Context;

public class ShadowMap {

    private static final int SHADOW_MAP_WIDTH = 2048;
    private static final int SHADOW_MAP_HEIGHT = 1536;

    private final Framebuffer framebuffer;
    private final Shader shader;

    private final OrthoFrustum frustum;

    public ShadowMap() {
        framebuffer = Framebuffer.create();
        shader = Shader.create();

        frustum = new OrthoFrustum();
    }

    public void update(Vector3f direction, PerspectiveCamera camera) {
        frustum.update(direction, camera);

        shader.bind();
        shader.setUniform("view", new Matrix4f().lookAt(new Vector3f(0.0f, 100.0f, 0.0f), new Vector3f(0.0f, 90.0f, 10.0f), new Vector3f(0.0f, 1.0f, 0.0f)));//frustum.getViewMatrix());
        shader.setUniform("projection", new Matrix4f().orthoSymmetric(100.0f, 100.0f, -100.0f, 1000.0f));//frustum.getProjectionMatrix());
    }

    public void init(Context context) {
        framebuffer.init(context, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT);
        framebuffer.attachColorBuffer();
        framebuffer.attachDepthBuffer();

        shader.init(context);
        shader.addSource(ShaderType.VERTEX, Utils.readFile("/shaders/shadowMap.vert"));
        shader.addSource(ShaderType.FRAGMENT, Utils.readFile("/shaders/shadowMap.frag"));
        shader.declareAttribute(0, "position");
        shader.link();
        shader.declareUniform("model");
        shader.declareUniform("view");
        shader.declareUniform("projection");
    }

    public void destroy() {
        framebuffer.destroy();
        shader.destroy();
    }

    public void bind() {
        framebuffer.bind();
        shader.bind();
        GL11.glViewport(0, 0, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT);
    }

    public void prepareModel(Matrix4f model) {
        shader.setUniform("model", model);
    }

    public Texture getTexture() { return framebuffer.getDepthTexture(); }
}
