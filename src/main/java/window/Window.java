package window;

import core.WindowAPI;

public interface Window {
    long getId();

    void init(int width, int height, String title);
    void show();

    int getWidth();
    int getHeight();
    float getAspectRatio();

    boolean keyDown(KeyCode key);
    boolean keyPressed(KeyCode key);
    boolean mouseButtonDown(MouseButton button);
    boolean mouseButtonPressed(MouseButton button);

    float getCursorX();
    float getCursorY();
    float getCursorDX();
    float getCursorDY();

    void setCursor(boolean enabled);
    void toggleCursor();

    boolean shouldClose();
    void update();

    void destroy();

    static Window create() {
        switch (WindowAPI.CURRENT) {
            case GLFW: return new GLFWWindow();
            default: throw new RuntimeException("Window API '" + WindowAPI.CURRENT + "' not supported currently");
        }
    }
}
