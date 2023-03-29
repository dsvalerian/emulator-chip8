package com.github.dsvalerian.chip8.exception;

/**
 * An exception that is thrown when an attempt is made to process instructions but no program is loaded.
 */
public class NoProgramLoadedException extends RuntimeException {
    /**
     * Default constructor, no messages needed.
     */
    public NoProgramLoadedException() {
        super();
    }
}
