package com.github.dsvalerian.chip8.util;

import com.github.dsvalerian.chip8.data.Bits;

/**
 * Defined constants that are used by the Chip-8 emulator.
 */
public class Constants {
    /**
     * The size of the Chip-8 RAM.
     */
    public static final int MEMORY_SIZE = 0x1000;

    /**
     * The number of bits used for each register in the memory.
     */
    public static final Bits MEMORY_BITS = Bits.EIGHT;

    /**
     * The size of the Chip-8 stack.
     */
    public static final int STACK_SIZE = 16;

    /**
     * The number of bits used for each register on in the stack.
     */
    public static final Bits STACK_BITS = Bits.SIXTEEN;

    /**
     * The number of bits used for the stack pointer register.
     */
    public static final Bits SP_REGISTER_BITS = Bits.EIGHT;

    /**
     * The number of Chip-8 general-purpose (Vx) registers.
     */
    public static final int NUM_V_REGISTERS = 16;

    /**
     * The number of bits used for each Vx register.
     */
    public static final Bits V_REGISTER_BITS = Bits.EIGHT;

    /**
     * The number of bits used for the index (I) register.
     */
    public static final Bits I_REGISTER_BITS = Bits.TWELVE;

    /**
     * The number of bits used for the program counter (PC) register.
     */
    public static final Bits PC_REGISTER_BITS = Bits.SIXTEEN;

    /**
     * The default location at which programs should be loaded in memory.
     */
    public static final int DEFAULT_PROGRAM_ADDRESS = 0x200;
}
