package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Register;

/**
 * Handles instruction processing.
 */
public interface Interpreter {
    /**
     * Decode and execute a Chip-8 instruction stored in a {@link Register}.
     *
     * @param instruction A {@link Register} holding the instruction as a 16-bit value.
     */
    void executeInstruction(Register instruction);
}
