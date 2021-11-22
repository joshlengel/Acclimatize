package render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import window.Context;

import java.nio.ByteBuffer;

public class OpenGLTexture implements Texture {

    private int id;

    public OpenGLTexture() {}

    public OpenGLTexture(int id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void init(Context context) {
        id = GL11.glGenTextures();
    }

    @Override
    public void destroy() {
        GL11.glDeleteTextures(id);
    }

    @Override
    public void load(int width, int height, ByteBuffer data) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    @Override
    public void bind(int slot) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }
}
