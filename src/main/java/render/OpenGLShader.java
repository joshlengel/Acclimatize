package render;

import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import window.Context;

import java.util.HashMap;
import java.util.Map;

public class OpenGLShader implements Shader {

    private int id;
    private final Map<ShaderType, Integer> shaderIds = new HashMap<>();
    private final Map<String, Integer> uniforms = new HashMap<>();

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void init(Context context) {
        id = GL20.glCreateProgram();
    }

    @Override
    public void destroy() {
        for (Integer shaderId : shaderIds.values()) {
            GL20.glDetachShader(id, shaderId);
            GL20.glDeleteShader(shaderId);
        }

        GL20.glDeleteProgram(id);
    }

    @Override
    public void addSource(ShaderType type, String source) {
        int glType;

        switch (type) {
            case VERTEX: glType = GL20.GL_VERTEX_SHADER; break;
            case GEOMETRY: glType = GL32.GL_GEOMETRY_SHADER; break;
            case FRAGMENT: glType = GL20.GL_FRAGMENT_SHADER; break;
            default: throw new RuntimeException("Must pass a valid ShaderType into Shader::addSource");
        }

        int shaderId = GL20.glCreateShader(glType);
        GL20.glShaderSource(shaderId, source);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) throw new RuntimeException("Error compiling shader (type: " + type + "): " + GL20.glGetShaderInfoLog(shaderId));

        GL20.glAttachShader(id, shaderId);

        shaderIds.put(type, shaderId);
    }

    @Override
    public void declareAttribute(int i, String name) {
        GL20.glBindAttribLocation(id, i, name);
    }

    @Override
    public void link() {
        GL20.glLinkProgram(id);

        if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) throw new RuntimeException("Error linking shader program: " + GL20.glGetProgramInfoLog(id));
    }

    @Override
    public void declareUniform(String name) {
        uniforms.put(name, GL20.glGetUniformLocation(id, name));
    }

    @Override
    public void bind() {
        GL20.glUseProgram(id);
    }

    @Override
    public void setUniform(String name, int i) {
        GL20.glUniform1i(uniforms.get(name), i);
    }

    @Override
    public void setUniform(String name, float f) {
        GL20.glUniform1f(uniforms.get(name), f);
    }

    @Override
    public void setUniform(String name, Vector2f v) {
        GL20.glUniform2f(uniforms.get(name), v.x, v.y);
    }

    @Override
    public void setUniform(String name, Vector3f v) {
        GL20.glUniform3f(uniforms.get(name), v.x, v.y, v.z);
    }

    @Override
    public void setUniform(String name, Vector4f v) {
        GL20.glUniform4f(uniforms.get(name), v.x, v.y, v.z, v.w);
    }

    @Override
    public void setUniform(String name, Vector2i v) {
        GL20.glUniform2i(uniforms.get(name), v.x, v.y);
    }

    @Override
    public void setUniform(String name, Vector3i v) {
        GL20.glUniform3i(uniforms.get(name), v.x, v.y, v.z);
    }

    @Override
    public void setUniform(String name, Vector4i v) {
        GL20.glUniform4i(uniforms.get(name), v.x, v.y, v.z, v.w);
    }

    @Override
    public void setUniform(String name, Matrix3f m) {
        float[] arr = new float[9];
        GL20.glUniformMatrix3fv(uniforms.get(name), true, m.get(arr));
    }

    @Override
    public void setUniform(String name, Matrix4f m) {
        float[] arr = new float[16];
        GL20.glUniformMatrix4fv(uniforms.get(name), true, m.get(arr));
    }
}
