package com.github.dsvalerian.chip8.data;

/**
 * Representation of a single register that can store a specified number of bits.
 */
public class Register {
    private Bits numBits;
    private long maxValue;
    private int value;

    /**
     * Construct a {@link Register} that is capable of storing a specified number of bits.
     *
     * @param numBits The number of bits this {@link Register} can store.
     */
    public Register(Bits numBits) {
        this.numBits = numBits;
        maxValue = (1 << numBits.getValue()) - 1;
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
     * @param value A {@link Register} that is storing the value to set.
     */
    public void set(Register value) {
        set(value.read());
    }

    /**
     * Set the value stored in this {@link Register}.
     *
     * @param value The value.
     */
    public void set(int value) {
        if (value > maxValue || value < 0) {
            throw new IllegalArgumentException("value " + value + " cannot be stored using " +
                    numBits.getValue() + " bits");
        }

        this.value = value;
    }
}
