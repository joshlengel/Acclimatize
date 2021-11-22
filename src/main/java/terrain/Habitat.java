package terrain;

import org.joml.Vector3f;

public interface Habitat {
    float getStrength(float x, float z);
    float getHeight(float x, float z);
    Vector3f getColor(float x, float z);
}
