import vbo.dd.VBOVertex2DColor3D;

import java.util.List;

public class Util {
    public static double[] pointListToDoubleArray(List<VBOVertex2DColor3D> pointList) {
        return pointList.stream().map(VBOVertex2DColor3D::toVBOArray).reduce((a, b) -> {
            double[] result = new double[a.length + b.length];
            System.arraycopy(a, 0, result, 0, a.length);
            System.arraycopy(b, 0, result, a.length, b.length);
            return result;
        }).orElse(new double[0]);
    }
}
