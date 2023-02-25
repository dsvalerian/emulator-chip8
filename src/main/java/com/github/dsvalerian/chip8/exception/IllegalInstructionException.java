package com.github.dsvalerian.chip8.exception;

/**
 * An exception that is thrown when an illegal Chip-8 instruction is provided to the interpreter.
 */
public class IllegalInstructionException extends RuntimeException {
    /**
     * Construct the exception with a specified instruction.
     *
     * @param instruction The instruction that caused this to be thrown.
     */
    public IllegalInstructionException(int instruction) {
        super(String.format("Illegal instruction 0x%04x provided.", instruction));
    }
}
