package render;

import core.GraphicsAPI;
import org.joml.*;
import window.Context;

public interface Shader {
    long getId();

    void init(Context context);
    void destroy();

    void addSource(ShaderType type, String source);
    void declareAttribute(int i, String name);
    void link();

    void declareUniform(String name);

    void bind();
    void setUniform(String name, int i);
    void setUniform(String name, float f);
    void setUniform(String name, Vector2f v);
    void setUniform(String name, Vector3f v);
    void setUniform(String name, Vector4f v);
    void setUniform(String name, Vector2i v);
    void setUniform(String name, Vector3i v);
    void setUniform(String name, Vector4i v);
    void setUniform(String name, Matrix3f m);
    void setUniform(String name, Matrix4f m);

    static Shader create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLShader();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
