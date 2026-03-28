package Model.Timer;

public class TimerFactory {

    public static MillisecondTimer getTickTimer() {
        return new MillisecondTimer();
    }


}
