package com.github.dsvalerian.chip8.cpu;

/**
 * Values representing the CPU speed in hertz.
 */
public enum CPUSpeed {
    /**
     * Half speed of the CPU, at 250hz.
     */
    HALF(250),

    /**
     * Full speed of the CPU, at 500hz.
     */
    FULL(500),

    /**
     * Double speed of the CPU, at 1000hz.
     */
    DOUBLE(1000);

    private int hertz;

    CPUSpeed(int hertz) {
        this.hertz = hertz;
    }

    /**
     * @return The speed value in hertz.
     */
    public int getHertz() {
        return hertz;
    }
}
