package vbo.dd;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import vbo.VBO;

import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class VBODynamic2D extends VBO {
    @Override
    protected int getBufferDataMode() {
        return GL30.GL_DYNAMIC_DRAW;
    }

    public void draw(int primCount) {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        bindBuffer();
        GL11.glVertexPointer(2, GL_DOUBLE, 0, 0);
        GL30.glDrawArrays(GL_LINE_LOOP, 0, primCount);
        unbindBuffer();
    }
}
