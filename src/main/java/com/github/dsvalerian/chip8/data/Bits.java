package com.github.dsvalerian.chip8.data;

/**
 * Represents a number of bits.
 */
public enum Bits {
    FOUR(4),
    EIGHT(8),
    TWELVE(12),
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
