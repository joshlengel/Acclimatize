package terrain;

import obj.Mesh;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import render.DataBuffer;
import render.IndexBuffer;
import render.VertexArray;
import render.VertexType;
import window.Context;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TerrainTile extends Mesh {

    private VertexArray vao;
    private DataBuffer[] buffers;
    private IndexBuffer indexBuffer;
    private final ByteBuffer colorData;

    public TerrainTile(float x, float z, int units, Biosphere biosphere) {
        vao = VertexArray.create();
        buffers = new DataBuffer[3];

        for (int i = 0; i < 3; ++i)
            buffers[i] = DataBuffer.create();

        indexBuffer = IndexBuffer.create();

        float size = units * biosphere.getUnitSize();

        colorData = BufferUtils.createByteBuffer(units * units * 6 * 3 * Float.BYTES);
        int index = 0;

        for (int i = 0; i < units; ++i)
        for (int j = 0; j < units; ++j) {
            float xMin = i * size / units + x;
            float xMax = (i + 1) * size / units + x;
            float zMin = j * size / units + z;
            float zMax = (j + 1) * size / units + z;

            Vector3f v1 = new Vector3f(xMin, biosphere.getHeight(xMin, zMin), zMin);
            Vector3f v2 = new Vector3f(xMax, biosphere.getHeight(xMax, zMin), zMin);
            Vector3f v3 = new Vector3f(xMin, biosphere.getHeight(xMin, zMax), zMax);
            Vector3f v4 = new Vector3f(xMax, biosphere.getHeight(xMax, zMax), zMax);

            positions.add(v1);
            positions.add(v2);
            positions.add(v4);
            positions.add(v1);
            positions.add(v4);
            positions.add(v3);

            Vector3f e1 = new Vector3f(v3).sub(v1);
            Vector3f e2 = new Vector3f(v2).sub(v1);
            Vector3f e3 = new Vector3f(v2).sub(v4);
            Vector3f e4 = new Vector3f(v3).sub(v4);

            Vector3f n1 = e1.cross(e2).normalize();
            Vector3f n2 = e3.cross(e4).normalize();
            normals.add(n1);
            normals.add(n1);
            normals.add(n1);
            normals.add(n2);
            normals.add(n2);
            normals.add(n2);

            Vector3f c1 = biosphere.getColor(v1.x, v1.z);
            Vector3f c2 = biosphere.getColor(v2.x, v2.z);
            Vector3f c3 = biosphere.getColor(v3.x, v3.z);
            Vector3f c4 = biosphere.getColor(v4.x, v4.z);

            Vector3f cSample1 = new Vector3f(c1).add(c2).add(c4).div(3.0f);
            Vector3f cSample2 = new Vector3f(c1).add(c4).add(c3).div(3.0f);

            for (int k = 0; k < 3; ++k) {
                colorData.putFloat(cSample1.x);
                colorData.putFloat(cSample1.y);
                colorData.putFloat(cSample1.z);
            }

            for (int k = 0; k < 3; ++k) {
                colorData.putFloat(cSample2.x);
                colorData.putFloat(cSample2.y);
                colorData.putFloat(cSample2.z);
            }

            indices.add(index++);
            indices.add(index++);
            indices.add(index++);
            indices.add(index++);
            indices.add(index++);
            indices.add(index++);
        }

        colorData.flip();
    }

    public void init(Context context) {
        vao.init(context);

        buffers[0].init(context, VertexType.VEC3);
        buffers[1].init(context, VertexType.VEC3);
        buffers[2].init(context, VertexType.VEC3);

        indexBuffer.init(context);

        vao.attachData(buffers[0]);
        vao.attachData(buffers[1]);
        vao.attachData(buffers[2]);
        vao.attachIndices(indexBuffer);

        ByteBuffer positionsBuffer = BufferUtils.createByteBuffer(positions.size() * 3 * Float.BYTES);
        for (Vector3f p : positions) {
            positionsBuffer.putFloat(p.x);
            positionsBuffer.putFloat(p.y);
            positionsBuffer.putFloat(p.z);
        }
        positionsBuffer.flip();

        ByteBuffer normalsBuffer = BufferUtils.createByteBuffer(normals.size() * 3 * Float.BYTES);
        for (Vector3f n : normals) {
            normalsBuffer.putFloat(n.x);
            normalsBuffer.putFloat(n.y);
            normalsBuffer.putFloat(n.z);
        }
        normalsBuffer.flip();

        buffers[0].setData(positionsBuffer);
        buffers[1].setData(normalsBuffer);
        buffers[2].setData(colorData);

        IntBuffer indexBuff = BufferUtils.createIntBuffer(indices.size());
        for (int i : indices)
            indexBuff.put(i);
        indexBuff.flip();

        indexBuffer.setData(indexBuff);
    }

    public void destroy() {
        vao.destroy();

        for (int i = 0; i < 3; ++i)
            buffers[i].destroy();

        indexBuffer.destroy();
    }

    public void render() {
        vao.render();
    }
}
