package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.gui.GUI;
import com.github.dsvalerian.chip8.io.KeyHandler;

import java.io.File;
import java.io.IOException;

/**
 * The main class of the program.
 */
public class Main {
    private static final GUI UI = GUI.getInstance();
    private static Emulator currentEmulator;
    private static Thread currentEmulatorThread;

    /**
     * The main method/entry point into the program. Doesn't do much on its own because
     * the {@link GUI} being statically assigned will run it, and then buttons from there
     * will trigger different methods.
     * @param args Program arguments. None are supported.
     */
    public static void main(String[] args) {
        UI.addKeyListener(KeyHandler.getInstance());
    }

    /**
     * Create and run an {@link Emulator} in a new thread.
     * @param romFile A Chip-8 rom file.
     */
    public static void createAndRunEmulatorForProgram(File romFile) {
        if (romFile == null) {
            System.err.println("No file provided.");
            return;
        }

        ROM rom = null;

        // Try opening the rom file.
        try {
            rom = ROM.fromFile(romFile);
        }
        catch (IOException ex) {
            System.err.println("Could not read ROM from file " + romFile.getName());
            ex.printStackTrace();
        }

        if (rom == null) {
            System.err.println("Failed loading ROM.");
        }
        else {
            GUI.updateTitleWithFileName(romFile.getName());
            startEmulator(rom);
        }
    }

    private static void startEmulator(ROM program) {
        if (currentEmulator != null) {
            currentEmulator.stop();
        }

        currentEmulator = new Emulator(program);
        currentEmulatorThread = new Thread(currentEmulator);
        currentEmulatorThread.start();
    }

    /**
     * @return The currently-running {@link Emulator}.
     */
    public static Emulator getCurrentEmulator() {
        return currentEmulator;
    }
}
