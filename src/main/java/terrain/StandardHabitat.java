package terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.function.BiFunction;

public class StandardHabitat implements Habitat {

    private final float strengthFrequency;
    private final float strengthVariance;
    private final Vector2f strengthOffset;

    private final BiFunction<Float, Float, Float> heightMap;
    private final BiFunction<Float, Float, Vector3f> colorMap;

    private final Noise noise;

    public StandardHabitat(float strengthFrequency, float strengthVariance, Vector2f strengthOffset, BiFunction<Float, Float, Float> heightMap, BiFunction<Float, Float, Vector3f> colorMap, Noise noise) {
        this.strengthFrequency = strengthFrequency;
        this.strengthVariance = strengthVariance;
        this.strengthOffset = strengthOffset;

        this.heightMap = heightMap;
        this.colorMap = colorMap;

        this.noise = noise;
    }

    @Override
    public float getStrength(float x, float z) {
        return (float)Math.pow(noise.sample(x + strengthOffset.x, z + strengthOffset.y, strengthFrequency), 1.0 / strengthVariance);
    }

    @Override
    public float getHeight(float x, float z) { return heightMap.apply(x, z); }

    @Override
    public Vector3f getColor(float x, float z) { return colorMap.apply(x, z); }
}
