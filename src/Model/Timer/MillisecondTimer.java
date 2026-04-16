package Model.Timer;

public class MillisecondTimer {
    private java.util.Timer _executor;

    public void start(long periodMs, Runnable task) {
        stop();

        _executor = new java.util.Timer(true);
        _executor.scheduleAtFixedRate(new java.util.TimerTask() {
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
        if (_executor!=null) {
            _executor.cancel();
            _executor=null;
        }
    }
}
