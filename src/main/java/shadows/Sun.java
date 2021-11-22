package shadows;

import org.joml.Vector3f;
import render.Shader;

public class Sun {

    private final Vector3f lightColor;
    private final Vector3f lightDir;

    public Sun(Vector3f lightColor, Vector3f lightDir) {
        this.lightColor = lightColor;
        this.lightDir = lightDir;
    }

    public void loadUniforms(Shader shader) {
        shader.setUniform("sunColor", lightColor);
        shader.setUniform("sunDir", lightDir);
    }

    public Vector3f getColor() { return lightColor; }
    public Vector3f getDirection() { return lightDir; }
}
