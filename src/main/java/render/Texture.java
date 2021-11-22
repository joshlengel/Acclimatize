package render;

import core.GraphicsAPI;
import window.Context;

import java.nio.ByteBuffer;

public interface Texture {

    long getId();

    void init(Context context);
    void destroy();

    void load(int width, int height, ByteBuffer data);
    void bind(int slot);
    default void bind() { bind(0); }

    static Texture create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLTexture();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
