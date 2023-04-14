package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.cpu.CPU;
import com.github.dsvalerian.chip8.cpu.CPUState;
import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.gui.GUI;
import com.github.dsvalerian.chip8.io.KeyState;
import com.github.dsvalerian.chip8.io.ScreenState;

/**
 * Representation of the entire Chip-8 emulator. Manages the {@link CPU}, {@link ScreenState}, {@link KeyState},
 * and GUI updates. Run in a new thread for each program that is loaded.
 */
public class Emulator implements Runnable {
    private static final int FPS = 60;
    private static final int FRAME_TIME_MS = 1000 / FPS;

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

        while (true) {
            if (shouldStop) {
                return;
            }

            if (paused == false) {
                update();
            }

            try {
                Thread.sleep(FRAME_TIME_MS);
            }
            catch (InterruptedException ex) {
                System.err.println("Emulator frame sleep interrupted.");
            }
        }
    }

    /**
     * Stop the emulator execution.
     */
    public void stop() {
        shouldStop = true;
    }

    // Updates once per frame.
    public void update() {
        if (cpu.hasMoreInstructions()) {
            cpu.processNextInstruction();
        }

        System.out.println(cpu);

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
