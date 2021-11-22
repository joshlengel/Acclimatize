package window;

import core.GraphicsAPI;
import org.joml.Vector4f;

public interface Context {
    void init(Window window);
    void destroy();

    void setClearColor(Vector4f color);
    void clear();

    void setDepthTesting(boolean enabled);
    void setBlending(boolean enabled);

    static Context create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLContext();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
