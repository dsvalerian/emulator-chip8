package com.github.dsvalerian.chip8.exception;

/**
 * An exception that is thrown when the CPU's subroutine stack is empty and tries to be popped.
 */
public class StackEmptyException extends RuntimeException {
    /**
     * Default constructor, no messages needed.
     */
    public StackEmptyException() {
        super();
    }
}
