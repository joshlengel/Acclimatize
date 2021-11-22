package render;

import core.GraphicsAPI;
import window.Context;

import java.nio.IntBuffer;

public interface IndexBuffer {
    long getId();

    void init(Context context);
    void destroy();

    void setData(IntBuffer data);
    int getIndexCount();

    static IndexBuffer create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLIndexBuffer();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
