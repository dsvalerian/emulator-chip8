package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.util.Constants;

public class Memory {
    // 4kb of memory
    public static final int MEMORY_SIZE = 0x1000;

    // Using ints instead of bytes because java has everything signed and treats all primitive numbers as ints
    private int[] memory = new int[MEMORY_SIZE];

    /**
     * Get a byte at a specified address.
     *
     * @param address The address to get the byte from.
     * @return The byte at the specified address.
     */
    public int get(int address) {
        if (address >= MEMORY_SIZE || address < 0x0) {
            throw new IllegalArgumentException("address must be within memory space of " + MEMORY_SIZE + " bytes");
        }

        return memory[address];
    }

    /**
     * Set a byte at a specified address.
     *
     * @param address The address to set.
     * @param value The byte value to set.
     */
    public void set(int address, int value) {
        if (address >= MEMORY_SIZE || address < 0x0) {
            throw new IllegalArgumentException("address must be within memory space of " + MEMORY_SIZE + " bytes");
        }

        if (value >= Constants.BYTE_SIZE || value < 0x0) {
            throw new IllegalArgumentException("value must be representable by one byte");
        }

        memory[address] = value;
    }
}
