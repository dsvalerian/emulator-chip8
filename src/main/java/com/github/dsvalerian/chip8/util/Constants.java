package com.github.dsvalerian.chip8.util;

/**
 * Defined constants that are used by the Chip-8 emulator.
 */
public class Constants {
    /**
     * The number of bits in half a byte.
     */
    public static final int FOUR_BIT_SIZE = 4;

    /**
     * The number of bits in a byte.
     */
    public static final int EIGHT_BIT_SIZE = 8;

    /**
     * The number of bytes in 1.5 bytes.
     */
    public static final int TWELVE_BIT_SIZE = 12;

    /**
     * The number of bits in two bytes.
     */
    public static final int SIXTEEN_BIT_SIZE = 16;

    /**
     * The size of the Chip-8 stack.
     */
    public static final int STACK_SIZE = 16;

    /**
     * The number of Chip-8 general-purpose registers.
     */
    public static final int NUM_REGISTERS = 16;

    /**
     * The size of the Chip-8 RAM.
     */
    public static final int MEMORY_SIZE = 0x1000;   // 4kb

    /**
     * The default location at which programs should be loaded in memory.
     */
    public static final int DEFAULT_PROGRAM_ADDRESS = 0x200;    // 512
}
