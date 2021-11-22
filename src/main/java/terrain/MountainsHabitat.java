package terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class MountainsHabitat extends StandardHabitat {

    private static final float STRENGTH_FREQUENCY = 0.005f;
    private static final float STRENGTH_VARIANCE = 0.9f;
    private static final Vector2f STRENGTH_OFFSET = new Vector2f(5.31f, -6.43f);

    private static final Vector2f HEIGHT_OFFSET = new Vector2f(-3.42f, -5.87f);
    private static final Vector2f HEIGHT_RANGE = new Vector2f(0.0f, 15.0f);

    private static final Vector2f COLOR_OFFSET = new Vector2f(-4.87f, 13.9f);

    public MountainsHabitat(Noise noise) {
        super(STRENGTH_FREQUENCY, STRENGTH_VARIANCE, STRENGTH_OFFSET,
            (x, z) -> {
                OctaveNoise on = new OctaveNoise(noise, 4, 0.08f, 1.5f, 0.7f);
                return (HEIGHT_RANGE.y - HEIGHT_RANGE.x) * on.sample(x + HEIGHT_OFFSET.x, z + HEIGHT_OFFSET.y) + HEIGHT_RANGE.x;
            },
            (x, z) -> {
                float shade = 0.2f * noise.sample(x + COLOR_OFFSET.x, z + COLOR_OFFSET.y, 0.5f) + 0.3f;
                return new Vector3f(shade);
            }, noise);
    }
}
