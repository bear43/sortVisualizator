package vbo;

import lombok.Data;
import org.lwjgl.opengl.GL30;

@Data
public abstract class VBO {
    private int id;

    public VBO() {
        id = GL30.glGenBuffers();
        System.out.println("Created VBO #" + id);
    }

    public void bindBuffer() {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, id);
    }

    public void unbindBuffer() {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    protected abstract int getBufferDataMode();

    public void fillBuffer(int[] array) {
        bindBuffer();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, array, getBufferDataMode());
        unbindBuffer();
    }

    public void fillBuffer(long[] array) {
        bindBuffer();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, array, getBufferDataMode());
        unbindBuffer();
    }

    public void fillBuffer(float[] array) {
        bindBuffer();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, array, getBufferDataMode());
        unbindBuffer();
    }

    public void fillBuffer(double[] array) {
        bindBuffer();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, array, getBufferDataMode());
        unbindBuffer();
    }

    public void free() {
        GL30.glDeleteBuffers(id);
        System.out.println("Deleted VBO #" + id);
    }
}
