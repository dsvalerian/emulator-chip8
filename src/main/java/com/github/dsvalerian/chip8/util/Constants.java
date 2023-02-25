package com.github.dsvalerian.chip8.util;

import com.github.dsvalerian.chip8.data.Bits;

public class Constants {
    /**
     * The number of bits in one byte.
     */
    public static final int ONE_BYTE_BITS = 8;

    /**
     * The number of bits in a Chip-8 instruction.
     */
    public static final Bits CHIP8_INSTRUCTION_BITS = Bits.SIXTEEN;

    /**
     * The size of each increment of the Chip-8 program counter.
     */
    public static final int CHIP8_PC_STEP_SIZE = CHIP8_INSTRUCTION_BITS.getValue() / ONE_BYTE_BITS;
}
