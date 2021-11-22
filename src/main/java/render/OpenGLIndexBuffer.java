package render;

import org.lwjgl.opengl.GL15;
import window.Context;

import java.nio.IntBuffer;

public class OpenGLIndexBuffer implements IndexBuffer {

    private int id;
    private int indexCount;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void init(Context context) {
        id = GL15.glGenBuffers();
    }

    @Override
    public void destroy() {
        GL15.glDeleteBuffers(id);
    }

    @Override
    public void setData(IntBuffer data) {
        indexCount = data.remaining();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
    }

    @Override
    public int getIndexCount() {
        return indexCount;
    }
}
