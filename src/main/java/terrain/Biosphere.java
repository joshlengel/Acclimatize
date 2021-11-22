package terrain;

import core.Utils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Biosphere {

    private static final int LERP_RADIUS = 3;
    private static final int LERP_AREA = (2 * LERP_RADIUS + 1) * (2 * LERP_RADIUS + 1);

    private final List<Habitat> habitats = new ArrayList<>();

    private final float unitSize;

    public Biosphere(Noise noise, float unitSize) {
        habitats.add(new GrassHabitat(noise));
        habitats.add(new MountainsHabitat(noise));
        habitats.add(new ForrestHabitat(noise));

        this.unitSize = unitSize;
    }

    public float getUnitSize() { return unitSize; }

    public Habitat getHabitat(float x, float z) {
        float maxStrength = Float.NEGATIVE_INFINITY;
        Habitat maxHabitat = null;

        for (Habitat habitat : habitats) {
            float strength = habitat.getStrength(x, z);

            if (strength > maxStrength) {
                maxStrength = strength;
                maxHabitat = habitat;
            }
        }

        return maxHabitat;
    }

    public float getHeight(float cx, float cz) {
        float heightAccum = 0.0f;

        for (int ix = -LERP_RADIUS; ix <= LERP_RADIUS; ++ix)
        for (int iz = -LERP_RADIUS; iz <= LERP_RADIUS; ++iz) {
            float x = cx + ix * unitSize;
            float z = cz + iz * unitSize;

            Habitat h = getHabitat(x, z);
            heightAccum += h.getHeight(x, z);
        }

        return heightAccum / LERP_AREA;
    }

    public float getPlayerHeight(float x, float z) {
        float sx = unitSize * (float)Math.floor(x / unitSize);
        float sz = unitSize * (float)Math.floor(z / unitSize);

        float v1 = getHeight(sx, sz);
        float v2 = getHeight(sx + unitSize, sz);
        float v3 = getHeight(sx, sz + unitSize);
        float v4 = getHeight(sx + unitSize, sz + unitSize);

        float r1 = (v2 - v1) * (x - sx) / unitSize + v1;
        float r2 = (v4 - v3) * (x - sx) / unitSize + v3;

        return (r2 - r1) * (z - sz) / unitSize + r1;
    }

    public Vector3f getColor(float cx, float cz) {
        Vector3f colorAccum = new Vector3f();
        boolean shouldLerp = false;
        Habitat current = getHabitat(cx, cz);

        for (int ix = -LERP_RADIUS; ix <= LERP_RADIUS; ++ix)
        for (int iz = -LERP_RADIUS; iz <= LERP_RADIUS; ++iz) {
            float x = cx + ix * unitSize;
            float z = cz + iz * unitSize;

            Habitat h = getHabitat(x, z);
            colorAccum.add(h.getColor(x, z));

            if (h != current) shouldLerp = true;
        }

        return shouldLerp? colorAccum.div(LERP_AREA) : current.getColor(cx, cz);
    }
}
