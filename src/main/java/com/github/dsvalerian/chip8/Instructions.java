package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.IllegalInstructionException;
import com.github.dsvalerian.chip8.util.Constants;

/**
 * This class handles executing the entire Chip-8 instruction set and updating {@link CPUState}s accordingly.
 */
public class Instructions {
    /**
     * Load an instruction from main memory in a {@link CPUState} into a {@link Register}.
     *
     * @param register The {@link Register} to load the instruction into. Must be 16-bit.
     * @param state The {@link CPUState} with the memory block and PC pointing to the instruction.
     */
    public static void load(CPUState state, Register register) {
        int instructionValue = 0;

        for (int i = 0; i < Constants.CHIP8_PC_STEP_SIZE; i++) {
            // Big endian, so bit shift the first values more, and the last isn't shifted at all.
            int byteValue = state.readMemory(state.readPc() + i);
            byteValue = byteValue << (Constants.CHIP8_PC_STEP_SIZE - i - 1) * Constants.ONE_BYTE_BITS;
            instructionValue += byteValue;
        }

        register.set(instructionValue);
    }

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
            case 0x3000:
                // 3xkk - Skip next instruction if Vx == kk
                seByte(state, instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x4000:
                // 4xkk - Skip next instruction if Vx != kk
                sneByte(state, instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x5000:
                switch (instruction & 0x000F) {
                    case 0x0:
                        // 5xy0 - Skip next instruction if Vx == Vy
                        seRegister(state, instruction & 0x0F00, instruction & 0x00F0);
                        break;
                    default:
                        throw new IllegalInstructionException(instruction);
                }
            case 0x6000:
                // 6xkk - Set Vx = kk
                ldByte(state, instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x7000:
                // 7xkk - Set Vx = Vx + kk
                addByte(state, instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x9000:
                switch (instruction & 0x000F) {
                    case 0x0:
                        // 9xy0 - Skip next instruction if Vx == Vy
                        sneRegister(state, instruction & 0x0F00, instruction & 0x00F0);
                        break;
                    default:
                        throw new IllegalInstructionException(instruction);
                }
            default:
                throw new IllegalInstructionException(instruction);
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

    private static void seByte(CPUState state, int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) == kk) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private static void seRegister(CPUState state, int x, int y) {
        // skip instruction if register Vx == Vy
        if (state.readV(x) == state.readV(y)) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private static void sneByte(CPUState state, int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) != kk) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private static void sneRegister(CPUState state, int x, int y) {
        // skip instruction if register Vx != Vy
        if (state.readV(x) != state.readV(y)) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private static void ldByte(CPUState state, int x, int kk) {
        state.setV(x, kk);
    }

    private static void addByte(CPUState state, int x, int kk) {
        state.setV(x, (state.readV(x) + kk) & 0x00FF);
    }
}
