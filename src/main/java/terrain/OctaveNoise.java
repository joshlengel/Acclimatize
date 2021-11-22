package terrain;

import org.joml.Vector2f;

import java.util.Random;

public class OctaveNoise {

    private final int numOctaves;
    private final float groundFrequency;
    private final float frequencyMultiplier;
    private final float amplitudeMultiplier;

    private final Noise noise;

    private final Vector2f[] offsets;

    public OctaveNoise(Noise noise, int numOctaves, float groundFrequency, float frequencyMultiplier, float amplitudeMultiplier) {
        this.noise = noise;

        this.numOctaves = numOctaves;
        this.groundFrequency = groundFrequency;
        this.frequencyMultiplier = frequencyMultiplier;
        this.amplitudeMultiplier = amplitudeMultiplier;

        this.offsets = new Vector2f[numOctaves];

        Random random = new Random(noise.getSeed());
        for (int i = 0; i < numOctaves; ++i) {
            this.offsets[i] = new Vector2f(random.nextFloat() * 1000.0f, random.nextFloat() * 1000.0f);
        }
    }

    public float sample(float x, float y) {
        float res = 0.0f;
        float amplitude = 1.0f;
        float frequency = groundFrequency;

        for (int i = 0; i < numOctaves; ++i) {
            res += amplitude * noise.sample(x + offsets[i].x, y + offsets[i].y, frequency);

            amplitude *= amplitudeMultiplier;
            frequency *= frequencyMultiplier;
        }

        return res;
    }
}
