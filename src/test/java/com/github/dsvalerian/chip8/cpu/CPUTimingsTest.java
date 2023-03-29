package com.github.dsvalerian.chip8.cpu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.dsvalerian.chip8.cpu.CPUTimings.NANOS_FREQUENCY;

public class CPUTimingsTest {
    private static final int CLOCK_TIME = NANOS_FREQUENCY / CPUSpeed.FULL.getHertz();

    private CPUState state;
    private CPUTimings timings;

    @BeforeEach
    public void setUp() {
        state = new CPUState();
        timings = new CPUTimings(CPUSpeed.FULL, state);
    }

    @Test
    public void finishCycleTest() {
        long startTime = System.nanoTime();
        timings.finishCycle(startTime);
        Assertions.assertTrue(System.nanoTime() > startTime + CLOCK_TIME);
    }

    @Test
    public void handleTimersTest() {
        state.setDt(60);
        state.setSt(120);

        long startTime = System.currentTimeMillis();
        int counter = 0;
        while (System.currentTimeMillis() < startTime + 510) {
            timings.handleTimers();
        }

        Assertions.assertTrue(state.readDt() <= 30);
        Assertions.assertTrue(state.readSt() <= 90);
    }
}
