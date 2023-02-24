package com.github.dsvalerian.chip8.data;

import com.github.dsvalerian.chip8.CPUState;
import com.github.dsvalerian.chip8.data.util.Constants;

public class Instructions {
    /**
     * Decode and execute a Chip-8 instruction stored in a {@link Register}.
     *
     * @param state The {@link CPUState} to use/alter when executing the instruction.
     * @param instruction A {@link Register} holding the instruction as a 16-bit value.
     */
    public static void execute(CPUState state, Register instruction) {
        execute(state, instruction.read());
    }

    /**
     * Decode and execute a Chip-8 instruction.
     *
     * @param state The {@link CPUState} to use/alter when executing the instruction.
     * @param instruction The instruction as a 16-bit value.
     */
    private static void execute(CPUState state, int instruction) {
        if (instruction >= 1 << Constants.CHIP8_INSTRUCTION_BITS.getValue() || instruction < 0) {
            throw new IllegalArgumentException("provided instruction is not a valid Chip-8 instruction");
        }

        // Decode the instruction by parts and delegate work to an instruction method.
        switch (instruction & 0x1000) {
            case 0x0000:
                switch(instruction & 0x0111) {
                    case 0x0E0:
                        // 00E0 - Clear the display.
                        cls(state);
                        break;
                    case 0x0EE:
                        // 00EE - Return from subroutine.
                        ret(state);
                        break;
                    default:
                        // 0nnn - Jump to machine code at address nnn.
                        sys(state, instruction & 0x0111);
                        break;
                }
                break;
            case 0x1000:
                // 1nnn - Jump to address nnn.
                jp(state, instruction & 0x0111);
                break;
            case 0x2000:
                // 2nnn - Call subroutine at address nnn.
                call(state, instruction & 0x0111);
                break;
            default:
                throw new IllegalArgumentException("provided instruction is not a valid Chip-8 instruction");
        }
    }

    private static void sys(CPUState state, int address) {
        // Apparently this should be ignored by modern interpreters.
    }

    private static void cls(CPUState state) {
        // todo clear the screen
    }

    private static void ret(CPUState state) {
        state.setPc(state.popStack());
    }

    private static void jp(CPUState state, int address) {
        state.setPc(address);
    }

    private static void call(CPUState state, int address) {
        state.pushStack(state.readPc());
        state.setPc(address);
    }
}
