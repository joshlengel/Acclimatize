package render;

import core.GraphicsAPI;
import window.Context;

import java.nio.ByteBuffer;

public interface DataBuffer {
    long getId();

    void init(Context context, VertexType type);
    void destroy();

    VertexType getType();

    void setData(ByteBuffer data);

    static DataBuffer create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLDataBuffer();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
