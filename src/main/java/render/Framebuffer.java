package render;

import core.GraphicsAPI;
import window.Context;

public interface Framebuffer {
    void init(Context context, int width, int height);
    void destroy();

    void attachColorBuffer();
    void attachDepthBuffer();
    void bind();

    Texture getColorTexture(int i);
    Texture getDepthTexture();

    static Framebuffer create() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLFramebuffer();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }

    static Framebuffer getScreenFramebuffer() {
        switch (GraphicsAPI.CURRENT) {
            case OPENGL: return new OpenGLFramebuffer();
            default: throw new RuntimeException("Graphics API '" + GraphicsAPI.CURRENT + "' not supported currently");
        }
    }
}
