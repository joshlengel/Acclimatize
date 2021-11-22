package render;

import org.lwjgl.opengl.GL15;
import window.Context;

import java.nio.ByteBuffer;

public class OpenGLDataBuffer implements DataBuffer {

    private int id;
    private VertexType type;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void init(Context context, VertexType type) {
        id = GL15.glGenBuffers();
        this.type = type;
    }

    @Override
    public void destroy() {
        GL15.glDeleteBuffers(id);
    }

    @Override
    public VertexType getType() {
        return type;
    }

    @Override
    public void setData(ByteBuffer data) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
    }
}
