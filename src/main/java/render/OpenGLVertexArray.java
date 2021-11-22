package render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import window.Context;

public class OpenGLVertexArray implements VertexArray {
    private int id;
    private int dataIndex = 0;
    private IndexBuffer indices;

    private static int currentlyBound = 0;

    @Override
    public long getId() {
        return id;
    }

    private void bind() {
        if (currentlyBound != id) {
            currentlyBound = id;
            GL30.glBindVertexArray(id);
        }
    }

    @Override
    public void init(Context context) {
        id = GL30.glGenVertexArrays();
    }

    @Override
    public void destroy() {
        GL30.glDeleteVertexArrays(id);
    }

    @Override
    public void attachData(DataBuffer buffer) {
        int type;
        int elementCount;

        switch (buffer.getType()) {
            case INT: type = GL11.GL_INT; elementCount = 1; break;
            case FLOAT: type = GL11.GL_FLOAT; elementCount = 1; break;
            case VEC2: type = GL11.GL_FLOAT; elementCount = 2; break;
            case VEC3: type = GL11.GL_FLOAT; elementCount = 3; break;
            case VEC4: type = GL11.GL_FLOAT; elementCount = 4; break;
            case IVEC2: type = GL11.GL_INT; elementCount = 2; break;
            case IVEC3: type = GL11.GL_INT; elementCount = 3; break;
            case IVEC4: type = GL11.GL_INT; elementCount = 4; break;
            default: throw new RuntimeException("Must pass a valid VertexType into DataBuffer::init");
        }

        bind();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, (int)buffer.getId());
        GL20.glVertexAttribPointer(dataIndex, elementCount, type, false, 0, 0L);
        GL20.glEnableVertexAttribArray(dataIndex);
        ++dataIndex;
    }

    @Override
    public void attachIndices(IndexBuffer buffer) {
        bind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, (int)buffer.getId());
        indices = buffer;
    }

    @Override
    public void render() {
        bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.getIndexCount(), GL11.GL_UNSIGNED_INT, 0L);
    }
}
