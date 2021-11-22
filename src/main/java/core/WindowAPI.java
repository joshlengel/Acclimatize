package core;

import java.util.List;

public enum WindowAPI {
    GLFW(0, Platform.ANY),
    WIN32(-1, Platform.WINDOWS),
    X11(-1, Platform.LINUX);

    private final int level;
    private final List<Platform> supportedPlatforms;

    public static final WindowAPI CURRENT;

    static {
        WindowAPI best = GLFW;

        for (WindowAPI api : WindowAPI.values()) {
            if (api.level > best.level && api.isSupported()) best = api;
        }

        CURRENT = best;
    }

    WindowAPI(int level, Platform... supportedPlatforms) {
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
