package render;

import core.GraphicsAPI;
import window.Context;
import window.OpenGLContext;

public interface VertexArray {
    long getId();

    void init(Context context);
    void destroy();

    void attachData(DataBuffer buffer);
    void attachIndices(IndexBuffer buffer);

    void render();

    static VertexArray create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLVertexArray();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
