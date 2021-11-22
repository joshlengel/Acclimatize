package core;

import java.util.Locale;

public enum Platform {
    WINDOWS,
    MACOS,
    LINUX,
    ANY;

    public static final Platform CURRENT;

    static {
        String osName = System.getProperty("os.name", "any").toLowerCase(Locale.ENGLISH);
        if (osName.contains("mac") || osName.contains("darwin")) CURRENT = MACOS;
        else if (osName.contains("win")) CURRENT = WINDOWS;
        else if (osName.contains("nux")) CURRENT = LINUX;
        else CURRENT = ANY;
    }

    boolean matches(Platform other) {
        return other == ANY || this == ANY || other == this;
    }
}
