package terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class GrassHabitat extends StandardHabitat {

    private static final float STRENGTH_FREQUENCY = 0.01f;
    private static final float STRENGTH_VARIANCE = 1.2f;
    private static final Vector2f STRENGTH_OFFSET = new Vector2f(1.89f, 3.8f);

    private static final Vector2f HEIGHT_OFFSET = new Vector2f(8.88f, -1.42f);
    private static final Vector2f HEIGHT_RANGE = new Vector2f(0.0f, 10.0f);

    private static final Vector2f COLOR_OFFSET = new Vector2f(-2.46f, -6.34f);

    public GrassHabitat(Noise noise) {
        super(STRENGTH_FREQUENCY, STRENGTH_VARIANCE, STRENGTH_OFFSET,
            (x, z) -> {
                OctaveNoise on = new OctaveNoise(noise, 3, 0.05f, 1.5f, 0.5f);
                return (HEIGHT_RANGE.y - HEIGHT_RANGE.x) * on.sample(x + HEIGHT_OFFSET.x, z + HEIGHT_OFFSET.y) + HEIGHT_RANGE.x;
            },
            (x, z) -> {
                return new Vector3f(0.0f, 0.2f * noise.sample(x + COLOR_OFFSET.x, z + COLOR_OFFSET.y, 0.5f) + 0.6f, 0.0f);
            }, noise);
    }
}
