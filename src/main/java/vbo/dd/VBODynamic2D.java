package vbo.dd;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import vbo.VBO;

import static org.lwjgl.opengl.GL11.*;

public class VBODynamic2D extends VBO {
    @Override
    protected int getBufferDataMode() {
        return GL30.GL_DYNAMIC_DRAW;
    }

    public void draw(int mode,int primCount) {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        bindBuffer();
        GL11.glVertexPointer(2, GL_DOUBLE, 40, 0);
        glColorPointer(3, GL_DOUBLE, 40, 16);
        GL30.glDrawArrays(mode, 0, primCount);
        unbindBuffer();
    }
}
