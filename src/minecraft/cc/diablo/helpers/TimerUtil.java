package cc.diablo.helpers;

public final class TimerUtil {
    private long time;
    private final long prevMS = 0L;

    public TimerUtil() {
        time = System.nanoTime() / 1000000L;
    }

    public boolean reach(final long time) {
        return time() >= time;
    }

    public void reset() {
        time = System.nanoTime() / 1000000L;
    }

    public boolean sleep(final long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }
    public short convertToMS(float perSecond) {
        return (short) (int) (1000.0F / perSecond);
    }
    public long time() {
        return System.nanoTime() / 1000000L - time;
    }

    public boolean delay(float milliSec) {
        return (float)MathHelper.getIncremental((double)(this.time() - this.prevMS), 50.0D) >= milliSec;
    }
}
