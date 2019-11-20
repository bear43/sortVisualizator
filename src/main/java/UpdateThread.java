public class UpdateThread implements Runnable {
    @Override
    public void run() {
        Main.bubbleSort(Main.array, () -> {
            Main.updatePointList();
            try {
                Thread.sleep(1);
            } catch (Exception ignored) {}
            return null;
        });
    }
}
