package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.util.Constants;

public class Registers {
    // Registers 0 through F, inclusive
    public static final int NUM_REGISTERS = 0x10;

    // Using ints instead of bytes because Java makes everything signed and treats primitive numbers as ints
    private int[] v = new int[NUM_REGISTERS];
    private int i;

    /**
     * Get the value at register Vx.
     *
     * @param register The "x" in "register Vx" -- can be from 0-F, inclusive.
     * @return The one-byte value at register Vx.
     */
    public int getV(int register) {
        if (register >= NUM_REGISTERS || register < 0) {
            throw new IllegalArgumentException("register must be 0-" + NUM_REGISTERS);
        }

        return v[register];
    }

    /**
     * Set the one-byte value at register Vx.
     *
     * @param register The "x" in "register Vx" -- can be from 0-F, inclusive.
     * @param value The one-byte value to set register Vx to.
     */
    public void setV(int register, int value) {
        if (register >= NUM_REGISTERS || register < 0) {
            throw new IllegalArgumentException("register must be 0-" + NUM_REGISTERS);
        }

        if (value >= Constants.BYTE_SIZE || value < 0x0) {
            throw new IllegalArgumentException("value must be representable by one byte");
        }

        v[register] = value;
    }

    /**
     * Get the value in the address register I.
     *
     * @return The one-byte value in register I.
     */
    public int getI() {
        return i;
    }

    /**
     * Set the value in address register I.
     *
     * @param value The one-byte value to set.
     */
    public void setI(int value) {
        if (value >= Memory.MEMORY_SIZE || value < 0x0) {
            throw new IllegalArgumentException("value must be representative of memory space");
        }

        i = value;
    }
}
