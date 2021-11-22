package obj;

import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Mesh {
    public final ArrayList<Vector3f> positions;
    public final ArrayList<Vector3f> normals;
    public final ArrayList<Integer> indices;
    public final ArrayList<Integer> materialIndices;

    public final ArrayList<Material> materials;

    public Mesh() {
        positions = new ArrayList<>();
        normals = new ArrayList<>();
        indices = new ArrayList<>();
        materialIndices = new ArrayList<>();

        materials = new ArrayList<>();
    }

    public void loadOBJ(String path) {
        class Vertex {
            public final Vector3f position;
            public final int index;

            public int normalIndex = -1;
            public boolean used = false;
            public Vertex duplicate = null;

            public Vertex(Vector3f position, int index) {
                this.position = position;
                this.index = index;
            }
        }

        this.positions.clear();
        this.normals.clear();
        this.indices.clear();
        this.materialIndices.clear();
        this.materials.clear();

        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();

        InputStream stream = Mesh.class.getResourceAsStream(path);
        if (stream == null) throw new RuntimeException("Error opening file '" + path + "'");

        HashMap<String, Material> objMaterials = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("mtllib ")) {
                    String libPath = line.split(" +")[1];
                    File libFile = new File(new File(path).getParentFile(), libPath);

                    InputStream libStream = Mesh.class.getResourceAsStream(libFile.getPath());
                    if (libStream == null)
                        throw new RuntimeException("Error opening material library '" + libFile.getPath() + "'");

                    try (BufferedReader libReader = new BufferedReader(new InputStreamReader(libStream))) {
                        String currentMaterialName = null;
                        Material currentMaterial = null;

                        while ((line = libReader.readLine()) != null) {
                            if (line.startsWith("newmtl ")) {
                                if (currentMaterial != null) {
                                    objMaterials.put(currentMaterialName, currentMaterial);
                                }

                                currentMaterialName = line.split(" +")[1];
                                currentMaterial = new Material();
                            } else if (line.startsWith("Ka ")) {
                                String[] components = line.split(" +");
                                currentMaterial.ambientColor = new Vector3f(
                                        Float.parseFloat(components[1]),
                                        Float.parseFloat(components[2]),
                                        Float.parseFloat(components[3])
                                );
                            } else if (line.startsWith("Kd ")) {
                                String[] components = line.split(" +");
                                currentMaterial.diffuseColor = new Vector3f(
                                        Float.parseFloat(components[1]),
                                        Float.parseFloat(components[2]),
                                        Float.parseFloat(components[3])
                                );
                            } else if (line.startsWith("Ks ")) {
                                String[] components = line.split(" +");
                                currentMaterial.specularColor = new Vector3f(
                                        Float.parseFloat(components[1]),
                                        Float.parseFloat(components[2]),
                                        Float.parseFloat(components[3])
                                );
                            } else if (line.startsWith("Ns ")) {
                                String[] components = line.split(" +");
                                currentMaterial.specularExponent = Float.parseFloat(components[1]);
                            }
                        }
                    }
                } else if (line.startsWith("v ")) {
                    String[] parts = line.split(" +");
                    vertices.add(new Vertex(
                            new Vector3f(
                                Float.parseFloat(parts[1]),
                                Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3])
                            ),
                            vertices.size()));
                } else if (line.startsWith("vn ")) {
                    String[] parts = line.split(" +");
                    normals.add(new Vector3f(
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3])
                    ));
                } else if (line.startsWith("usemtl ")) {
                    String mtlName = line.split(" +")[1];
                    Material mtl = objMaterials.get(mtlName);

                    // Assume every material only appears once
                    materials.add(mtl);
                } else if (line.startsWith("f ")) break;
            }

            if (line == null) return;

            do {
                if (line.startsWith("f ")) {
                    String[] vs = line.split(" +");

                    int mtlIndex = materials.size() - 1;
                    materialIndices.add(mtlIndex);
                    materialIndices.add(mtlIndex);
                    materialIndices.add(mtlIndex);

                    for (int i = 1; i <= 3; ++i) {
                        String[] parts = vs[i].split("/");
                        int positionsIndex = Integer.parseInt(parts[0]) - 1;
                        int normalsIndex = -1;

                        if (parts.length >= 3) {
                            normalsIndex = Integer.parseInt(parts[2]) - 1;
                        }

                        Vertex v = vertices.get(positionsIndex);
                        if (!v.used) {
                            v.used = true;
                            v.normalIndex = normalsIndex;
                            this.indices.add(v.index);
                        } else if (v.normalIndex == normalsIndex) {
                            this.indices.add(v.index);
                        } else {
                            Vertex current = v;
                            boolean found = false;

                            while (current.duplicate != null) {
                                current = current.duplicate;

                                if (current.normalIndex == normalsIndex) {
                                    this.indices.add(current.index);
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                Vertex next = new Vertex(v.position, vertices.size());
                                next.used = true;
                                next.normalIndex = normalsIndex;

                                current.duplicate = next;
                                vertices.add(next);
                                this.indices.add(next.index);
                            }
                        }
                    }
                } else if (line.startsWith("usemtl ")) {
                    String mtlName = line.split(" +")[1];
                    Material mtl = objMaterials.get(mtlName);

                    // Assume every material only appears once
                    materials.add(mtl);
                }
            } while ((line = reader.readLine()) != null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Vertex v : vertices) {
            this.positions.add(v.position);
            if (v.normalIndex >= 0) this.normals.add(normals.get(v.normalIndex));
        }
    }
}
