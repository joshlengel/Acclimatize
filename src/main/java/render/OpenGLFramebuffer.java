package render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import window.Context;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class OpenGLFramebuffer implements Framebuffer {

    private int width, height;

    private int id;
    private static int currentId;

    private final List<Integer> colorTextures = new ArrayList<>();
    private int depthTexture;

    @Override
    public void init(Context context, int width, int height) {
        this.width = width;
        this.height = height;

        id = GL30.glGenFramebuffers();
    }

    @Override
    public void destroy() {
        GL30.glDeleteFramebuffers(id);

        for (int texture : colorTextures) GL11.glDeleteTextures(texture);
        if (depthTexture != 0) GL11.glDeleteTextures(depthTexture);
    }

    @Override
    public void attachColorBuffer() {
        bind();

        int attachment = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, attachment);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + colorTextures.size(), GL11.GL_TEXTURE_2D, attachment, 0);
        colorTextures.add(attachment);
    }

    @Override
    public void attachDepthBuffer() {
        bind();

        if (depthTexture != 0) GL11.glDeleteTextures(depthTexture);
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer)null);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }

    @Override
    public void bind() {
        if (currentId != id) {
            currentId = id;
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
        }
    }

    @Override
    public Texture getColorTexture(int i) {
        return new OpenGLTexture(colorTextures.get(i));
    }

    @Override
    public Texture getDepthTexture() {
        return new OpenGLTexture(depthTexture);
    }
}
