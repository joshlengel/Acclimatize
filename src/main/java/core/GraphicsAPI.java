package core;

import java.util.List;

public enum GraphicsAPI {
    OPENGL(0, Platform.WINDOWS, Platform.LINUX),
    VULKAN(-1, Platform.WINDOWS, Platform.LINUX),
    METAL(-1, Platform.MACOS);

    private final int level;
    private final List<Platform> supportedPlatforms;

    public static final GraphicsAPI CURRENT;

    static {
        GraphicsAPI best = OPENGL;

        for (GraphicsAPI api : GraphicsAPI.values()) {
            if (api.level > best.level && api.isSupported()) best = api;
        }

        CURRENT = best;
    }

    GraphicsAPI(int level, Platform... supportedPlatforms) {
        this.level = level;
        this.supportedPlatforms = List.of(supportedPlatforms);
    }

    public boolean isSupported() {
        for (Platform platform : supportedPlatforms) {
            if (platform.matches(Platform.CURRENT)) return true;
        }

        return false;
    }
}
