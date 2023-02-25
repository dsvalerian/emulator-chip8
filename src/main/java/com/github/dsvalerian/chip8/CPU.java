package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.ROM;

/**
 * Representation of the full system. In charge of all the necessary parts for loading and
 * executing programs.
 */
public interface CPU {
    /**
     * Read the next instruction and execute it.
     */
    void processNextInstruction();

    /**
     * @return True if there are still more instructions in the loaded program for the CPU to process.
     */
    boolean hasMoreInstructions();

    /**
     * Load a {@link ROM} into main memory and reset the program counter.
     *
     * @param program The {@link ROM} to load.
     */
    void loadProgram(ROM program);
}
