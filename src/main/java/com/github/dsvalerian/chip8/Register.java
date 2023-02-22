package com.github.dsvalerian.chip8;

/**
 * Representation of a single register, where the size of the register (in bits) is assigned.
 */
public class Register {
    private long maxSize;
    private int value;

    /**
     * Construct a {@link Register} that is capable of storing a specified number of bits.
     *
     * @param numBits The number of bits this {@link Register} can store.
     */
    public Register(int numBits) {
        if (numBits > 32) {
            throw new IllegalArgumentException("cannot assign more than 32 bits to a register");
        }

        maxSize = 1 << numBits;
    }

    /**
     * Read the value stored in this {@link Register}.
     *
     * @return The value.
     */
    public int read() {
        return value;
    }

    /**
     * Set the value stored in this {@link Register}.
     *
     * @param value The value.
     */
    public void set(int value) {
        if (value >= maxSize || value < 0) {
            throw new IllegalArgumentException("value cannot be outside of register space 0-" + (maxSize - 1));
        }

        this.value = value;
    }
}
