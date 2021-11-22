package core;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import render.Texture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public final class Utils {
    private Utils() {}

    public static String readFile(String path) {
        InputStream stream = Utils.class.getResourceAsStream(path);
        if (stream == null) throw new RuntimeException("Error opening file '" + path + "'");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder source = new StringBuilder();
            String line = reader.readLine();

            source.append(line);

            while ((line = reader.readLine()) != null) source.append('\n').append(line);
            return source.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadFromFile(Texture texture, String path) {
        InputStream stream = Utils.class.getResourceAsStream(path);
        if (stream == null) throw new RuntimeException("Error opening file '" + path + "'");

        ByteBuffer buffer;
        try {
            byte[] bytes = stream.readAllBytes();
            buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.flip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];
        ByteBuffer data = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
        if (data == null) throw new RuntimeException("Error loading texture '" + path + "': " + STBImage.stbi_failure_reason());
        texture.load(width[0], height[0], data);
    }
}
