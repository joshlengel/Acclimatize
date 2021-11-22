package obj;

import game.Renderable;
import org.lwjgl.BufferUtils;
import render.*;
import window.Context;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class MeshObject extends Mesh implements Renderable {

    private final VertexArray vao;
    private final DataBuffer positionsBuffer, normalsBuffer;
    private final IndexBuffer indexBuffer;

    private final Material mtl;

    public MeshObject(Material mtl) {
        this.vao = VertexArray.create();
        this.positionsBuffer = DataBuffer.create();
        this.normalsBuffer = DataBuffer.create();
        this.indexBuffer = IndexBuffer.create();

        this.mtl = mtl;
    }

    public void init(Context context) {
        vao.init(context);
        positionsBuffer.init(context, VertexType.VEC3);
        normalsBuffer.init(context, VertexType.VEC3);
        indexBuffer.init(context);

        vao.attachData(positionsBuffer);
        vao.attachData(normalsBuffer);
        vao.attachIndices(indexBuffer);
    }

    public void destroy() {
        vao.destroy();
        positionsBuffer.destroy();
        normalsBuffer.destroy();
        indexBuffer.destroy();
    }

    public void prepare(Shader shader) {
        shader.setUniform("material.ambientColor", mtl.ambientColor);
        shader.setUniform("material.diffuseColor", mtl.diffuseColor);
        shader.setUniform("material.specularColor", mtl.specularColor);
        shader.setUniform("material.specularExponent", mtl.specularExponent);
    }

    @Override
    public void render() {
        vao.render();
    }
}
/*
public class MeshRenderable extends Mesh implements Renderable {
    private MeshObject[] meshObjects;

    @Override
    public void loadOBJ(String path) {
        super.loadOBJ(path);

        int mtlIndex = 0;
        int i = 0;
        int iStart = 0;

        while (mtlIndex < materials.size()) {
            while (materialIndices.get(i) == mtlIndex) ++i;

            if (i != iStart) {
                // Load data
                ByteBuffer positionsData = BufferUtils.createByteBuffer(() * 3 * Float.BYTES);
                positions.forEach(position -> {
                    positionsData.putFloat(position.x);
                    positionsData.putFloat(position.y);
                    positionsData.putFloat(position.z);
                });
                positionsData.flip();

                ByteBuffer normalsData = BufferUtils.createByteBuffer(normals.size() * 3 * Float.BYTES);
                normals.forEach(normal -> {
                    normalsData.putFloat(normal.x);
                    normalsData.putFloat(normal.y);
                    normalsData.putFloat(normal.z);
                });
                normalsData.flip();

                IntBuffer indexData = BufferUtils.createIntBuffer(indices.size());
                indices.forEach(indexData::put);
                indexData.flip();
                indexData.position(0);
            }
        }

        positionsBuffer.setData(positionsData);
        normalsBuffer.setData(normalsData);
        indexBuffer.setData(indexData);
    }

    @Override
    public void render() {

    }
}*/