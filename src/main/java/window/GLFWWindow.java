package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.HashMap;
import java.util.Map;

public class GLFWWindow implements Window {

    private long window;
    private int width, height;
    private float aspectRatio;

    private final Map<KeyCode, Boolean> keys = new HashMap<>();
    private final Map<MouseButton, Boolean> buttons = new HashMap<>();

    private float oldCx, oldCy, newCx, newCy;

    private static final Map<KeyCode, Integer> GLFW_KEYCODES = new HashMap<>();
    private static final Map<MouseButton, Integer> GLFW_MOUSEBUTTONS = new HashMap<>();

    static {
        GLFW_KEYCODES.put(KeyCode.KEY_A, GLFW.GLFW_KEY_A);
        GLFW_KEYCODES.put(KeyCode.KEY_B, GLFW.GLFW_KEY_B);
        GLFW_KEYCODES.put(KeyCode.KEY_C, GLFW.GLFW_KEY_C);
        GLFW_KEYCODES.put(KeyCode.KEY_D, GLFW.GLFW_KEY_D);
        GLFW_KEYCODES.put(KeyCode.KEY_E, GLFW.GLFW_KEY_E);
        GLFW_KEYCODES.put(KeyCode.KEY_F, GLFW.GLFW_KEY_F);
        GLFW_KEYCODES.put(KeyCode.KEY_G, GLFW.GLFW_KEY_G);
        GLFW_KEYCODES.put(KeyCode.KEY_H, GLFW.GLFW_KEY_H);
        GLFW_KEYCODES.put(KeyCode.KEY_I, GLFW.GLFW_KEY_I);
        GLFW_KEYCODES.put(KeyCode.KEY_J, GLFW.GLFW_KEY_J);
        GLFW_KEYCODES.put(KeyCode.KEY_K, GLFW.GLFW_KEY_K);
        GLFW_KEYCODES.put(KeyCode.KEY_L, GLFW.GLFW_KEY_L);
        GLFW_KEYCODES.put(KeyCode.KEY_M, GLFW.GLFW_KEY_M);
        GLFW_KEYCODES.put(KeyCode.KEY_N, GLFW.GLFW_KEY_N);
        GLFW_KEYCODES.put(KeyCode.KEY_O, GLFW.GLFW_KEY_O);
        GLFW_KEYCODES.put(KeyCode.KEY_P, GLFW.GLFW_KEY_P);
        GLFW_KEYCODES.put(KeyCode.KEY_Q, GLFW.GLFW_KEY_Q);
        GLFW_KEYCODES.put(KeyCode.KEY_R, GLFW.GLFW_KEY_R);
        GLFW_KEYCODES.put(KeyCode.KEY_S, GLFW.GLFW_KEY_S);
        GLFW_KEYCODES.put(KeyCode.KEY_T, GLFW.GLFW_KEY_T);
        GLFW_KEYCODES.put(KeyCode.KEY_U, GLFW.GLFW_KEY_U);
        GLFW_KEYCODES.put(KeyCode.KEY_V, GLFW.GLFW_KEY_V);
        GLFW_KEYCODES.put(KeyCode.KEY_W, GLFW.GLFW_KEY_W);
        GLFW_KEYCODES.put(KeyCode.KEY_X, GLFW.GLFW_KEY_X);
        GLFW_KEYCODES.put(KeyCode.KEY_Y, GLFW.GLFW_KEY_Y);
        GLFW_KEYCODES.put(KeyCode.KEY_Z, GLFW.GLFW_KEY_Z);
        GLFW_KEYCODES.put(KeyCode.KEY_0, GLFW.GLFW_KEY_0);
        GLFW_KEYCODES.put(KeyCode.KEY_1, GLFW.GLFW_KEY_1);
        GLFW_KEYCODES.put(KeyCode.KEY_2, GLFW.GLFW_KEY_2);
        GLFW_KEYCODES.put(KeyCode.KEY_3, GLFW.GLFW_KEY_3);
        GLFW_KEYCODES.put(KeyCode.KEY_4, GLFW.GLFW_KEY_4);
        GLFW_KEYCODES.put(KeyCode.KEY_5, GLFW.GLFW_KEY_5);
        GLFW_KEYCODES.put(KeyCode.KEY_6, GLFW.GLFW_KEY_6);
        GLFW_KEYCODES.put(KeyCode.KEY_7, GLFW.GLFW_KEY_7);
        GLFW_KEYCODES.put(KeyCode.KEY_8, GLFW.GLFW_KEY_8);
        GLFW_KEYCODES.put(KeyCode.KEY_9, GLFW.GLFW_KEY_9);
        GLFW_KEYCODES.put(KeyCode.KEY_LEFT, GLFW.GLFW_KEY_LEFT);
        GLFW_KEYCODES.put(KeyCode.KEY_RIGHT, GLFW.GLFW_KEY_RIGHT);
        GLFW_KEYCODES.put(KeyCode.KEY_DOWN, GLFW.GLFW_KEY_DOWN);
        GLFW_KEYCODES.put(KeyCode.KEY_UP, GLFW.GLFW_KEY_UP);
        GLFW_KEYCODES.put(KeyCode.KEY_SPACE, GLFW.GLFW_KEY_SPACE);
        GLFW_KEYCODES.put(KeyCode.KEY_BACKSPACE, GLFW.GLFW_KEY_BACKSPACE);
        GLFW_KEYCODES.put(KeyCode.KEY_ENTER, GLFW.GLFW_KEY_ENTER);
        GLFW_KEYCODES.put(KeyCode.KEY_ESCAPE, GLFW.GLFW_KEY_ESCAPE);

        GLFW_MOUSEBUTTONS.put(MouseButton.MB_LEFT, GLFW.GLFW_MOUSE_BUTTON_LEFT);
        GLFW_MOUSEBUTTONS.put(MouseButton.MB_RIGHT, GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        GLFW_MOUSEBUTTONS.put(MouseButton.MB_MIDDLE, GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
    }

    public GLFWWindow() {
        for (KeyCode key : KeyCode.values()) keys.put(key, false);
        for (MouseButton button : MouseButton.values()) buttons.put(button, false);
    }

    @Override
    public long getId() {
        return window;
    }

    @Override
    public void init(int width, int height, String title) {
        if (!GLFW.glfwInit()) throw new RuntimeException("Error initializing GLFW");

        window = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
        if (window == 0L) throw new RuntimeException("Error creating window");

        this.width = width;
        this.height = height;
        this.aspectRatio = (float)width / (float)height;

        GLFW.glfwSetWindowSizeCallback(window, (win, w, h) -> {
            this.width = w;
            this.height = h;
            this.aspectRatio = (float)w / (float)h;
        });

        GLFWVidMode screen = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (screen == null) throw new RuntimeException("Error retrieving monitor information");
        GLFW.glfwSetWindowPos(window, (screen.width() - width) / 2, (screen.height() - height) / 2);

        GLFW.glfwMakeContextCurrent(window);

        oldCx = newCx = getCursorX();
        oldCy = newCy = getCursorY();
    }

    @Override
    public void show() {
        GLFW.glfwShowWindow(window);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public boolean keyDown(KeyCode key) {
        switch (key) {
            case KEY_SHIFT:
                return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

            case KEY_ALT:
                return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;

            case KEY_CTRL:
                return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;

            default:
                return GLFW.glfwGetKey(window, GLFW_KEYCODES.get(key)) == GLFW.GLFW_PRESS;
        }
    }

    @Override
    public boolean keyPressed(KeyCode key) {
        return keyDown(key) && !keys.get(key);
    }

    @Override
    public boolean mouseButtonDown(MouseButton button) {
        return GLFW.glfwGetMouseButton(window, GLFW_MOUSEBUTTONS.get(button)) == GLFW.GLFW_PRESS;
    }

    @Override
    public boolean mouseButtonPressed(MouseButton button) {
        return mouseButtonDown(button) && !buttons.get(button);
    }

    @Override
    public float getCursorX() {
        return newCx;
    }

    @Override
    public float getCursorY() {
        return newCy;
    }

    @Override
    public float getCursorDX() {
        return newCx - oldCx;
    }

    @Override
    public float getCursorDY() {
        return newCy - oldCy;
    }

    @Override
    public void setCursor(boolean enabled) {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, enabled? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED);

        oldCx = newCx = getCursorX();
        oldCy = newCy = getCursorY();
    }

    @Override
    public void toggleCursor() {
        setCursor(GLFW.glfwGetInputMode(window, GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_NORMAL);
    }

    @Override
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    @Override
    public void update() {
        GLFW.glfwSwapBuffers(window);

        for (KeyCode key : KeyCode.values()) keys.put(key, keyDown(key));
        for (MouseButton button : MouseButton.values()) buttons.put(button, mouseButtonDown(button));

        oldCx = newCx;
        oldCy = newCy;

        double[] new_cx = new double[1];
        double[] new_cy = new double[1];
        GLFW.glfwGetCursorPos(window, new_cx, new_cy);
        newCx = (float)new_cx[0] / (float)width * 2 - 1;
        newCy = 1 - (float)new_cy[0] / (float)height * 2;

        GLFW.glfwPollEvents();
    }

    @Override
    public void destroy() {
        GLFW.glfwTerminate();
    }
}
