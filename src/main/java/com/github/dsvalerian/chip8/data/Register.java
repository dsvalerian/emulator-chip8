package com.github.dsvalerian.chip8.data;

/**
 * Representation of a single register that can store a specified number of bits.
 */
public class Register {
    private Bits size;
    private long maxValue;
    private int value;

    /**
     * Construct a {@link Register} that is capable of storing a specified number of bits.
     *
     * @param size The size, in bits, of this {@link Register}.
     */
    public Register(Bits size) {
        this.size = size;
        maxValue = (1 << size.getValue()) - 1;
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
                    size.getValue() + " bits");
        }

        this.value = value;
    }
}
