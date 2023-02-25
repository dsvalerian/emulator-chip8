package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Register;

/**
 * This class handles executing the entire Chip-8 instruction set and updating {@link CPUState}s accordingly.
 */
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
        // Decode the instruction by parts and delegate work to an instruction method.
        switch (instruction & 0xF000) {
            case 0x0000:
                switch(instruction & 0x0FFF) {
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
                        sys(state, instruction & 0x0FFF);
                        break;
                }
                break;
            case 0x1000:
                // 1nnn - Jump to address nnn.
                jp(state, instruction & 0x0FFF);
                break;
            case 0x2000:
                // 2nnn - Call subroutine at address nnn.
                call(state, instruction & 0x0FFF);
                break;
            default:
                throw new IllegalArgumentException("provided instruction is not a valid Chip-8 instruction");
        }
    }

    private static void sys(CPUState state, int address) {
        // Apparently this should be ignored by modern interpreters. Do nothing.
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
