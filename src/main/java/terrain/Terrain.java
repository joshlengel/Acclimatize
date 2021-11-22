package terrain;

import game.Player;
import org.joml.Vector2f;
import window.Context;

import java.util.*;

public class Terrain {

    private static final int RENDER_DISTANCE = 4;

    private final int tileUnits;
    private final float tileSize;
    private final Map<Vector2f, TerrainTile> tiles = new HashMap<>();

    private final Biosphere biosphere;

    public Terrain(float cx, float cz, int tileUnits, Biosphere biosphere) {
        this.tileUnits = tileUnits;
        this.tileSize = tileUnits * biosphere.getUnitSize();

        this.biosphere = biosphere;
    }

    public void update(Context context, float camX, float camZ) {
        float cx = tileSize * (float)Math.floor(camX / tileSize);
        float cz = tileSize * (float)Math.floor(camZ / tileSize);

        for (Iterator<Map.Entry<Vector2f, TerrainTile>> itr = tiles.entrySet().iterator(); itr.hasNext();) {
            Map.Entry<Vector2f, TerrainTile> entry = itr.next();
            if (Math.abs(entry.getKey().x - cx) > RENDER_DISTANCE * tileSize
                || Math.abs(entry.getKey().y - cz) > RENDER_DISTANCE * tileSize) {
                entry.getValue().destroy();
                itr.remove();
            }
        }

        for (int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; ++x)
        for (int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; ++z) {
            Vector2f pos = new Vector2f(cx + x * tileSize, cz + z * tileSize);
            if (!tiles.containsKey(pos)) {
                TerrainTile tile = new TerrainTile(pos.x, pos.y, tileUnits, biosphere);
                tiles.put(pos, tile);
                tile.init(context);
            }
        }
    }

    public void destroy() {
        for (TerrainTile tile : tiles.values()) tile.destroy();
    }

    public void render() {
        for (TerrainTile tile : tiles.values()) tile.render();
    }

    public void updatePlayer(Player player) {
        float terrainHeight = biosphere.getPlayerHeight(player.position.x, player.position.z);

        if (player.position.y < terrainHeight) {
            player.position.y = terrainHeight;
            player.velocity.y = 0.0f;
        }
    }
}
