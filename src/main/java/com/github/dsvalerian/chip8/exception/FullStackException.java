package com.github.dsvalerian.chip8.exception;

/**
 * An exception that is thrown when the CPU's subroutine stack is full.
 */
public class FullStackException extends RuntimeException {
    /**
     * Default constructor, no messages needed.
     */
    public FullStackException() {
        super();
    }
}
