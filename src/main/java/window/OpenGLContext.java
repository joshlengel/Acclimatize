package window;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class OpenGLContext implements Context {
    @Override
    public void init(Window window) {
        GL.createCapabilities();
    }

    @Override
    public void destroy() {}

    @Override
    public void setClearColor(Vector4f color) {
        GL11.glClearColor(color.x, color.y, color.z, color.w);
    }

    @Override
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setDepthTesting(boolean enabled) {
        if (enabled) GL11.glEnable(GL11.GL_DEPTH_TEST);
        else GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void setBlending(boolean enabled) {
        if (enabled) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
}
