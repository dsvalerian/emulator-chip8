package com.github.dsvalerian.chip8.exception;

/**
 * An exception that is thrown when an unsupported instruction is provided to the interpreter.
 */
public class UnsupportedInstructionException extends RuntimeException {
    /**
     * Construct the exception with a specified instruction.
     *
     * @param instruction The instruction that caused this to be thrown.
     */
    public UnsupportedInstructionException(int instruction) {
        super(String.format("Unsupported instruction 0x%04x provided.", instruction));
    }
}
