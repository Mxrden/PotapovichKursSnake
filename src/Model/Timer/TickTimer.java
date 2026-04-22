package Model.Timer;

import javax.swing.Timer;

public class TickTimer {

    private Timer _executor;

    public void start(int periodMs, Runnable task) {
        stop();

        _executor = new Timer(periodMs, e -> {
            try {
                task.run();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        _executor.setRepeats(true);
        _executor.start();
    }

    public void stop() {
        if (_executor != null) {
            _executor.stop();
            _executor = null;
        }
    }
}
