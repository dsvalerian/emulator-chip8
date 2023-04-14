package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.cpu.CPU;
import com.github.dsvalerian.chip8.cpu.CPUSpeed;
import com.github.dsvalerian.chip8.cpu.CPUState;
import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.gui.FPS;
import com.github.dsvalerian.chip8.gui.GUI;
import com.github.dsvalerian.chip8.io.KeyState;
import com.github.dsvalerian.chip8.io.ScreenState;

/**
 * Representation of the entire Chip-8 emulator. Manages the {@link CPU}, {@link ScreenState}, {@link KeyState},
 * and GUI updates. Run in a new thread for each program that is loaded.
 */
public class Emulator implements Runnable {
    private static final FPS FRAMES_PER_SECOND = FPS.SIXTY;
    private static final CPUSpeed CPU_SPEED = CPUSpeed.FULL;
    private static final int FRAME_TIME_NANO = 1000000000 / FRAMES_PER_SECOND.getValue();
    private static final int CPU_TIME_NANO = 1000000000 / CPU_SPEED.getHertz();

    private static final GUI UI = GUI.getInstance();
    private ScreenState screenState;
    private CPU cpu;
    private ROM program;
    private boolean paused;

    private boolean shouldStop = false;

    /**
     * Create a new {@link Emulator}.
     * @param program The {@link ROM} program this emulator will run.
     */
    public Emulator(ROM program) {
        this.program = program;

        CPUState state = new CPUState();
        screenState = new ScreenState();
        cpu = new CPU(state, screenState);
    }

    /**
     * Run the emulator. Use in a new {@link Thread}.
     */
    @Override
    public void run() {
        cpu.loadProgram(program);
        long lastUpdate = 0;
        long lastFrame = 0;

        while (true) {
            if (shouldStop) {
                return;
            }

            if (paused == false) {
                // Check that it's time to update the cpu.
                if (System.nanoTime() - lastUpdate >= CPU_TIME_NANO) {
                    update();
                }

                // Check that it's time to draw a frame.
                if (System.nanoTime() - lastFrame >= FRAME_TIME_NANO) {
                    draw();
                }
            }
        }
    }

    /**
     * Stop the emulator execution.
     */
    public void stop() {
        shouldStop = true;
    }

    /**
     * Gets run once per CPU update.
     */
    public void update() {
        if (cpu.hasMoreInstructions()) {
            cpu.processNextInstruction();
        }

        //System.out.println(cpu);
    }

    /**
     * Gets run once per frame.
     */
    public void draw() {
        UI.drawScreen(screenState);
    }

    /**
     * Pauses the emulator.
     */
    public void pause() {
        paused = true;
    }

    /**
     * Unpauses the emulator.
     */
    public void resume() {
        paused = false;
    }

    /**
     * @return True if the emulator is currently processing updates, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }
}
