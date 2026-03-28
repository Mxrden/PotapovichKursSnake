package Model.Timer;

public class MillisecondTimer {
    private java.util.Timer executor;

    public void start(long periodMs, Runnable task) {
        stop();

        executor = new java.util.Timer(true);
        executor.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run(){
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, periodMs, periodMs);
    }


    public void stop() {
        if (executor!=null) {
            executor.cancel();
            executor=null;
        }
    }
}
