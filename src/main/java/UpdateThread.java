import vbo.dd.VBOVertex2DColor3D;

import java.util.ArrayList;
import java.util.List;

public class UpdateThread implements Runnable {

    private static final int DELAY_START = 1000;

    @Override
    public void run() {
        System.out.println("starting...");
        try {
            System.out.println("waiting " + DELAY_START + "ms");
            Thread.sleep(DELAY_START);
        } catch (Exception ignored) {}
        System.out.println("go!");
        Main.bubbleSort(Main.array, (indexA, indexB) -> {
            Main.additionalList.clear();
            VBOVertex2DColor3D firstLineA = new VBOVertex2DColor3D(0, 0, 1.0, 0.0, 0.0);
            VBOVertex2DColor3D current;
            Main.additionalList.add(firstLineA);
            current = Main.pointList.get(indexA);
            Main.additionalList.add(new VBOVertex2DColor3D(current.getX(), current.getY(), 1.0, 0.0, 0.0));
            Main.additionalList.add(new VBOVertex2DColor3D(firstLineA.getX(), firstLineA.getY(), 0.25, 1.0, 0.0));
            current = Main.pointList.get(indexB);
            Main.additionalList.add(new VBOVertex2DColor3D(current.getX(), current.getY(), 0.25, 1.0, 0.0));
            Main.updatePointList();
            try {
                Thread.sleep(1, 100);
            } catch (Exception ignored) {}
        });
    }
}
