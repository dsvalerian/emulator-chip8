package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.cpu.CPU;
import com.github.dsvalerian.chip8.gui.MainWindow;
import com.github.dsvalerian.chip8.io.Keyboard;
import com.github.dsvalerian.chip8.io.Screen;

/**
 * Representation of the entire Chip-8 machine. Manages the {@link CPU}, {@link Screen}, and {@link Keyboard}.
 */
public class Emulator {
    private static MainWindow gui;

    public static void main(String[] args) {
        gui = new MainWindow();

        // TODO this is where the emulator starts
    }
}
