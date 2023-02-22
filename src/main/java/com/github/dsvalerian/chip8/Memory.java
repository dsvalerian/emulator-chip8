package com.github.dsvalerian.chip8;

public class Memory {
    // 4kb of memory
    public static final int SIZE = 0x1000;

    // Using ints instead of bytes because java has everything signed and treats all primitive numbers as ints
    private int[] memory = new int[SIZE];

    /**
     * Get a byte at a specified address.
     *
     * @param address The address to get the byte from.
     * @return The byte at the specified address.
     */
    public int get(int address) {
        if (address >= SIZE || address < 0) {
            throw new IllegalArgumentException("address must be within memory space of " + SIZE + " bytes");
        }

        return memory[address];
    }

    /**
     * Set a byte at a specified address.
     *
     * @param address The address to set.
     * @param value The byte value.
     */
    public void set(int address, int value) {
        if (address >= SIZE || address < 0) {
            throw new IllegalArgumentException("address must be within memory space of " + SIZE + " bytes");
        }

        if (value >= 256 || value < 0) {
            throw new IllegalArgumentException("value must be representable by one byte");
        }

        memory[address] = value;
    }
}
