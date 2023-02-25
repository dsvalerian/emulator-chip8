package com.github.dsvalerian.chip8.data;

/**
 * Represents a number of bits.
 */
public enum Bits {
    /**
     * Four bits.
     */
    FOUR(4),

    /**
     * Eight bits.
     */
    EIGHT(8),

    /**
     * Twelve bits.
     */
    TWELVE(12),

    /**
     * Sixteen bits.
     */
    SIXTEEN(16);

    private int value;

    Bits(int value) {
        this.value = value;
    }

    /**
     * @return The number of bits.
     */
    public int getValue() {
        return value;
    }
}
