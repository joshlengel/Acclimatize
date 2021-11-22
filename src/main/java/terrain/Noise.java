package terrain;

import org.joml.Vector2f;

import java.util.Random;

public class Noise {

    private static final int GRID_SIZE = 256;

    private final long seed;
    private final Vector2f[] grid;

    public Noise(long seed) {
        this.seed = seed;
        grid = new Vector2f[GRID_SIZE * GRID_SIZE];

        Random random = new Random(seed);

        for (int x = 0; x < GRID_SIZE; ++x)
        for (int y = 0; y < GRID_SIZE; ++y) {
            double angle = random.nextDouble() * 2 * Math.PI;
            Vector2f v = new Vector2f((float)Math.sin(angle), (float)Math.cos(angle));

            grid[y * GRID_SIZE + x] = v;
        }
    }

    private float smoothstep(float x) {
        return (6 * x * x - 15 * x + 10) * x * x * x;
    }

    private float lerp(float weight, float y0, float y1) {
        return weight * (y1 - y0) + y0;
    }

    private int mod(int a, int b) {
        return a - Math.floorDiv(a, b) * b;
    }

    public float sample(float x, float y, float frequency) {
        float sx = x * frequency;
        float sy = y * frequency;
        Vector2f pos = new Vector2f(sx, sy);

        float xMin = (float)Math.floor(sx);
        float xMax = xMin + 1.0f;

        float yMin = (float)Math.floor(sy);
        float yMax = yMin + 1.0f;

        int ixMin = mod((int)xMin, GRID_SIZE);
        int ixMax = ixMin == GRID_SIZE - 1? 0 : ixMin + 1;

        int iyMin = mod((int)yMin, GRID_SIZE);
        int iyMax = iyMin == GRID_SIZE - 1? 0 : iyMin + 1;

        float dp1 = grid[iyMin * GRID_SIZE + ixMin].dot(new Vector2f(xMin, yMin).sub(pos));
        float dp2 = grid[iyMin * GRID_SIZE + ixMax].dot(new Vector2f(xMax, yMin).sub(pos));
        float dp3 = grid[iyMax * GRID_SIZE + ixMin].dot(new Vector2f(xMin, yMax).sub(pos));
        float dp4 = grid[iyMax * GRID_SIZE + ixMax].dot(new Vector2f(xMax, yMax).sub(pos));

        float weightX = smoothstep(sx - xMin);
        float weightY = smoothstep(sy - yMin);

        float result = lerp(weightY, lerp(weightX, dp1, dp2), lerp(weightX, dp3, dp4));
        return (result + 1.0f) * 0.5f;
    }

    public long getSeed() { return seed; }
}
