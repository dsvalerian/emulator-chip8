package com.github.dsvalerian.chip8.cpu;

public class CPUTimings {
    protected static final int NANOS_FREQUENCY = 1000000000;
    private static final int TIMERS_FREQUENCY = 60;
    private static final long TIMERS_PERIOD = NANOS_FREQUENCY / TIMERS_FREQUENCY;
    private final int CYCLE_DELTA;

    private CPUState state;
    private long lastTimerTime;

    /**
     * Create a new {@link CPUTimings} set at a certain speed.
     * @param speed The speed of the CPU.
     */
    public CPUTimings(CPUSpeed speed, CPUState state) {
        CYCLE_DELTA = NANOS_FREQUENCY / speed.getHertz();
        this.state = state;
    }

    /**
     * Wait until the end of the CPU clock cycle.
     * @param startTime The start of the current clock cycle, in nanoseconds.
     */
    public void finishCycle(long startTime) {
        long endTime = startTime + CYCLE_DELTA;
        while (System.nanoTime() < endTime);
    }

    /**
     * Decrement the delay and sound timers at a rate of 60hz per second.
     * If it's been a longer duration such that they should be decremented more than once,
     * they will be.
     * If it hasn't been long enough to be decremented at all, then they won't be.
     */
    public void handleTimers() {
        /*
         * If the state is paused, then we're not decrementing any timers, and instead will
         * reset the lastTimerTime to the current time so that the time difference keeps up.
         * If lastTimerTime == 0, then it hasn't been set before so we just set it for the
         * first time.
         */
        if (state.isPaused() || lastTimerTime == 0) {
            lastTimerTime = System.nanoTime();
            return;
        }

        long timeDifference = System.nanoTime() - lastTimerTime;
        long periods = timeDifference / TIMERS_PERIOD;

        if (periods > 0) {
            for (int i = 0; i < periods; i++) {
                int newDt = state.readDt() - 1;
                int newSt = state.readSt() - 1;
                state.setDt(newDt < 0 ? 0 : newDt);
                state.setSt(newSt < 0 ? 0 : newSt);
            }

            lastTimerTime = System.nanoTime();
        }
    }
}
