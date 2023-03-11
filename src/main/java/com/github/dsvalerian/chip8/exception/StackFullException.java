package com.github.dsvalerian.chip8.exception;

/**
 * An exception that is thrown when the CPU's subroutine stack is full and is attempted to be pushed to.
 */
public class StackFullException extends RuntimeException {
    /**
     * Default constructor, no messages needed.
     */
    public StackFullException() {
        super();
    }
}
