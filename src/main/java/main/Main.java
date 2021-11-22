package main;

import core.Utils;
import game.Player;
import game.PerspectiveCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import render.*;
import shadows.ShadowMap;
import terrain.Biosphere;
import terrain.Noise;
import terrain.Terrain;
import window.Context;
import window.KeyCode;
import window.Window;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Main {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "Acclimatize 1.0.0 alpha";

    private static final Vector3f SKY_COLOR = new Vector3f(0.0f, 0.75f, 0.72f);
    private static final Vector3f SUN_DIR = new Vector3f(-0.3f, -0.8f, 0.2f).normalize();

    private static Window window;
    private static Context context;

    private static Noise noise;
    private static Biosphere biosphere;
    private static Terrain terrain;
    private static PerspectiveCamera camera;
    private static Player player;

    private static Shader shader;

    private static ShadowMap shadowMap;

    public static void main(String[] args) {
        // Window and context
        window = Window.create();
        window.init(WIDTH, HEIGHT, TITLE);

        context = Context.create();
        context.init(window);
        context.setClearColor(new Vector4f(SKY_COLOR, 1.0f));
        context.setDepthTesting(true);

        // Model
        noise = new Noise(System.nanoTime());

        biosphere = new Biosphere(noise, 1.0f);
        terrain = new Terrain(0.0f, 0.0f, 10, biosphere);

        // Camera
        camera = new PerspectiveCamera(window);

        player = new Player(window);
        player.speed = 5.0f;
        player.position.y = 15.0f;
        camera.setController(player);

        // Shader
        shader = Shader.create();
        shader.init(context);
        shader.addSource(ShaderType.VERTEX, Utils.readFile("/shaders/default.vert"));
        shader.addSource(ShaderType.FRAGMENT, Utils.readFile("/shaders/default.frag"));
        shader.declareAttribute(0, "position");
        shader.declareAttribute(1, "normal");
        shader.declareAttribute(2, "color");
        shader.link();
        shader.declareUniform("projection");
        shader.declareUniform("view");
        shader.declareUniform("model");
        shader.declareUniform("skyColor");
        shader.declareUniform("sunDir");
        shader.declareUniform("shineDamp");
        shader.declareUniform("reflectivity");

        shader.bind();
        shader.setUniform("skyColor", SKY_COLOR);
        shader.setUniform("sunDir", SUN_DIR);

        // Shadows
        /*shadowMap = new ShadowMap();
        shadowMap.init(context);
        shadowMap.update(SUN_DIR, camera);*/

        // TEST
        /*VertexArray vao = VertexArray.create();
        vao.init(context);

        DataBuffer vbo = DataBuffer.create();
        vbo.init(context, VertexType.VEC2);

        IndexBuffer ibo = IndexBuffer.create();
        ibo.init(context);

        ByteBuffer vertices = BufferUtils.createByteBuffer(4 * 2 * Float.BYTES);
        vertices.putFloat(-1.0f);
        vertices.putFloat(-1.0f);
        vertices.putFloat( 1.0f);
        vertices.putFloat(-1.0f);
        vertices.putFloat(-1.0f);
        vertices.putFloat( 1.0f);
        vertices.putFloat( 1.0f);
        vertices.putFloat( 1.0f);
        vertices.flip();

        IntBuffer indices = BufferUtils.createIntBuffer(6);
        indices.put(0);
        indices.put(1);
        indices.put(3);
        indices.put(0);
        indices.put(3);
        indices.put(2);
        indices.flip();
        indices.position(0);

        vbo.setData(vertices);
        ibo.setData(indices);

        vao.attachData(vbo);
        vao.attachIndices(ibo);

        Shader quadShader = Shader.create();
        quadShader.init(context);
        quadShader.addSource(ShaderType.VERTEX, "#version 130\nin vec2 position;\nout vec2 uv;\nvoid main() { gl_Position = vec4(position, 0.0, 1.0); uv=vec2((position.x + 1) * 0.5, (1 - position.y) * 0.5); }");
        quadShader.addSource(ShaderType.FRAGMENT, "#version 130\nin vec2 uv;\nout vec4 color;\nuniform sampler2D samp;\nvoid main() { color=vec4((texture(samp, uv).rrr + 1)*0.5, 1.0); }");
        quadShader.declareAttribute(0, "position");
        quadShader.link();*/

        window.show();
        window.setCursor(false);

        float t1, t2;
        t1 = System.nanoTime() * 1e-9f;

        while (!window.shouldClose()) {
            t2 = System.nanoTime() * 1e-9f;
            float dt = t2 - t1;
            t1 = t2;

            // Update + physics
            window.update();
            if (window.keyPressed(KeyCode.KEY_ESCAPE)) window.toggleCursor();

            camera.controller.updateCamera(camera);
            //shadowMap.update(SUN_DIR, camera);

            player.update(dt);
            terrain.updatePlayer(player);

            terrain.update(context, camera.position.x, camera.position.z);

            /*// Render shadow map
            Matrix4f modelMatrix = new Matrix4f();

            shadowMap.bind();
            GL11.glDrawBuffer(GL11.GL_NONE);
            context.clear();

            shadowMap.prepareModel(modelMatrix);
            terrain.render();*/

            /*// Render depth texture
            Framebuffer.getScreenFramebuffer().bind();
            GL11.glDrawBuffer(GL11.GL_BACK);
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            context.clear();

            quadShader.bind();
            shadowMap.getTexture().bind();
            vao.render();*/

            context.clear();

            shader.bind();
            shader.setUniform("projection", camera.getProjectionMatrix());
            shader.setUniform("view", camera.getViewMatrix());
            shader.setUniform("model", new Matrix4f());
            terrain.render();
        }

        //shadowMap.destroy();

        terrain.destroy();
        shader.destroy();

        context.destroy();
        window.destroy();
    }
}
