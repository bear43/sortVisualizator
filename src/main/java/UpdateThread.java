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
        Main.bubbleSort(Main.array, () -> {
            Main.updatePointList();
            try {
                Thread.sleep(0, 100);
            } catch (Exception ignored) {}
            return null;
        });
    }
}
