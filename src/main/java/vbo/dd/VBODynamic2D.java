package vbo.dd;

import org.lwjgl.opengl.GL30;
import vbo.VBO;

public class VBODynamic2D extends VBO {
    @Override
    protected int getBufferDataMode() {
        return GL30.GL_DYNAMIC_DRAW;
    }
}
