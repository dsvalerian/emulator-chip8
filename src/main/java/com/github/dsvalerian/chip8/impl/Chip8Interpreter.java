package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPUProfile;
import com.github.dsvalerian.chip8.CPUState;
import com.github.dsvalerian.chip8.Interpreter;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.IllegalInstructionException;

import static com.github.dsvalerian.chip8.util.Constants.*;

/**
 * {@inheritDoc}
 *
 * The default implementation of {@link Interpreter}.
 */
public class Chip8Interpreter implements Interpreter {
    private final CPUProfile PROFILE = CPUProfile.CHIP8;
    private final int PC_STEP_SIZE = PROFILE.getInstructionBits().getValue() / BYTE;

    private Chip8CPUState state;

    /**
     * Constructs a {@link Chip8Interpreter} with an assigned {@link Chip8CPUState}.
     *
     * @param state The {@link CPUState} that will be used when processing instructions.
     */
    public Chip8Interpreter(Chip8CPUState state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeInstruction(Register instruction) {
        executeInstruction(instruction.read());
    }

    private void executeInstruction(int instruction) {
        // Decode the instruction by parts and delegate work to an instruction method.
        switch (instruction & 0xF000) {
            case 0x0000:
                switch(instruction & 0x0FFF) {
                    // 00E0
                    case 0x0E0: cls(); break;
                    // 00EE
                    case 0x0EE: ret(); break;
                    // 0nnn
                    default: sys(getNnn(instruction)); break;
                }
                break;
            // 1nnn
            case 0x1000: jp(getNnn(instruction)); break;
            // 2nnn
            case 0x2000: call(getNnn(instruction)); break;
            // 3xkk
            case 0x3000: seByte(getX(instruction), getKk(instruction)); break;
            // 4xkk
            case 0x4000: sneByte(getX(instruction), getKk(instruction)); break;
            case 0x5000:
                switch (instruction & 0x000F) {
                    // 5xy0
                    case 0x0: seRegister(getX(instruction), getY(instruction)); break;
                    default: throw new IllegalInstructionException(instruction);
                }
                break;
            // 6xkk
            case 0x6000: ldByte(getX(instruction), getKk(instruction)); break;
            // 7xkk
            case 0x7000: addByte(getX(instruction), getKk(instruction)); break;
            case 0x8000:
                switch (instruction & 0x000F) {
                    // 8xy0
                    case 0x0: ldRegister(getX(instruction), getY(instruction)); break;
                    // 8xy1
                    case 0x1: or(getX(instruction), getY(instruction)); break;
                    // 8xy2
                    case 0x2: and(getX(instruction), getY(instruction)); break;
                    // 8xy3
                    case 0x3: xor(getX(instruction), getY(instruction)); break;
                    // 8xy4
                    case 0x4: addRegister(getX(instruction), getY(instruction)); break;
                    // 8xy5
                    case 0x5: sub(getX(instruction), getY(instruction)); break;
                    // 8xy6
                    case 0x6: shr(getX(instruction), getY(instruction)); break;
                    // 8xy7
                    case 0x7: subn(getX(instruction), getY(instruction)); break;
                    // 8xyE
                    case 0xE: shl(getX(instruction), getY(instruction)); break;
                    default: throw new IllegalInstructionException(instruction);
                }
                break;
            case 0x9000:
                switch (instruction & 0x000F) {
                    // 9xy0
                    case 0x0: sneRegister(getX(instruction), getY(instruction)); break;
                    default: throw new IllegalInstructionException(instruction);
                }
                break;
            default: throw new IllegalInstructionException(instruction);
        }
    }

    /**
     * Get the x value, aka the second nibble, from an instruction.
     *
     * @param instruction The instruction to get the x value from.
     * @return The second nibble in the instruction.
     */
    private int getX(int instruction) {
        return (instruction & 0x0F00) >> BYTE;
    }

    /**
     * Get the y value, aka the third nibble, from an instruction.
     *
     * @param instruction The instruction to get the y value from.
     * @return The third nibble in the instruction.
     */
    private int getY(int instruction) {
        return (instruction & 0x00F0) >> NIBBLE;
    }

    /**
     * Get the kk value, aka the last byte, from an instruction.
     *
     * @param instruction The instruction to get the kk value from.
     * @return The last byte in the instruction.
     */
    private int getKk(int instruction) {
        return instruction & 0x00FF;
    }

    /**
     * Get the nnn value, aka the last three nibbles, from an instruction.
     *
     * @param instruction The instruction to get the nnn value from.
     * @return The last three nibbles in the instruction.
     */
    private int getNnn(int instruction) {
        return instruction & 0x0FFF;
    }

    /**
     * 0nnn - SYS addr
     * This instruction is only used on the old computers on which Chip-8 was originally implemented.
     * It is ignored by modern interpreters.
     */
    private void sys(int address) {
        // Ignore.
    }

    /**
     * 00E0 - CLS
     * Clear the display.
     */
    private void cls() {
        // todo clear the screen
    }

    /**
     * 00EE - RET
     * The interpreter sets the program counter to the address at the top of the stack,
     * then subtracts 1 from the stack pointer.
     */
    private void ret() {
        state.setPc(state.popStack());
    }

    /**
     * 1nnn - JP addr
     * The interpreter sets the program counter to nnn.
     */
    private void jp(int address) {
        state.setPc(address);
    }

    /**
     * 2nnn - CALL addr
     * The interpreter increments the stack pointer, then puts the current PC on the top of the stack.
     * The PC is then set to nnn.
     */
    private void call(int address) {
        state.pushStack(state.readPc());
        state.setPc(address);
    }

    /**
     * 3xkk - SE Vx, byte
     * The interpreter compares register Vx to kk, and if they are equal,
     * increments the program counter by 2.
     */
    private void seByte(int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) == kk) {
            state.setPc(state.readPc() + PC_STEP_SIZE);
        }
    }

    /**
     * 5xy0 - SE Vx, Vy
     * The interpreter compares register Vx to register Vy, and if they are equal,
     * increments the program counter by 2.
     */
    private void seRegister(int x, int y) {
        // skip instruction if register Vx == Vy
        if (state.readV(x) == state.readV(y)) {
            state.setPc(state.readPc() + PC_STEP_SIZE);
        }
    }

    /**
     * 4xkk - SNE Vx, byte
     * The interpreter compares register Vx to kk, and if they are not equal,
     * increments the program counter by 2.
     */
    private void sneByte(int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) != kk) {
            state.setPc(state.readPc() + PC_STEP_SIZE);
        }
    }

    /**
     * 9xy0 - SNE Vx, Vy
     * The values of Vx and Vy are compared, and if they are not equal,
     * the program counter is increased by 2.
     */
    private void sneRegister(int x, int y) {
        // skip instruction if register Vx != Vy
        if (state.readV(x) != state.readV(y)) {
            state.setPc(state.readPc() + PC_STEP_SIZE);
        }
    }

    /**
     * 6xkk - LD Vx, byte
     * The interpreter puts the value kk into register Vx.
     */
    private void ldByte(int x, int kk) {
        state.setV(x, kk);
    }

    /**
     * 8xy0 - LD Vx, Vy
     * Stores the value of register Vy in register Vx.
     */
    private void ldRegister(int x, int y) {
        state.setV(x, state.readV(y));
    }

    /**
     * 7xkk - ADD Vx, byte
     * Adds the value kk to the value of register Vx, then stores the result in Vx.
     */
    private void addByte(int x, int kk) {
        int value = state.readV(x) + kk;
        state.setV(x, value & 0x00FF);
    }

    /**
     * 8xy4 - ADD Vx, Vy
     * The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., > 255,)
     * VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored in Vx.
     */
    private void addRegister(int x, int y) {
        int value = state.readV(x) + state.readV(y);
        int carry = value > 0x00FF ? 1 : 0;

        state.setV(x, value & 0x00FF);
        state.setV(0xF, carry);
    }

    /**
     * 8xy1 - OR Vx, Vy
     * Performs a bitwise OR on the values of Vx and Vy, then stores the result in Vx.
     * A bitwise OR compares the corrseponding bits from two values, and if either bit is 1,
     * then the same bit in the result is also 1. Otherwise, it is 0.
     */
    private void or(int x, int y) {
        state.setV(x, state.readV(x) | state.readV(y));
    }

    /**
     * 8xy2 - AND Vx, Vy
     * Performs a bitwise AND on the values of Vx and Vy, then stores the result in Vx.
     * A bitwise AND compares the corrseponding bits from two values, and if both bits are 1,
     * then the same bit in the result is also 1. Otherwise, it is 0.
     */
    private void and(int x, int y) {
        state.setV(x, state.readV(x) & state.readV(y));
    }

    /**
     * 8xy3 - XOR Vx, Vy
     * Performs a bitwise exclusive OR on the values of Vx and Vy, then stores the result in Vx.
     * An exclusive OR compares the corrseponding bits from two values, and if the bits are not both the same,
     * then the corresponding bit in the result is set to 1. Otherwise, it is 0.
     */
    private void xor(int x, int y) {
        state.setV(x, state.readV(x) ^ state.readV(y));
    }

    /**
     * 8xy5 - SUB Vx, Vy
     * If Vx > Vy, then VF is set to 1, otherwise 0. Then Vy is subtracted from Vx,
     * and the results stored in Vx.
     */
    private void sub(int x, int y) {
        int notBorrow = state.readV(x) > state.readV(y) ? 1 : 0;
        int value = state.readV(x) - state.readV(y);

        state.setV(x, value & 0x00FF);
        state.setV(0xF, notBorrow);
    }

    /**
     * 8xy7 - SUBN Vx, Vy
     * If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy,
     * and the results stored in Vx.
     */
    private void subn(int x, int y) {
        int notBorrow = state.readV(y) > state.readV(x) ? 1 : 0;
        int value = state.readV(y) - state.readV(x);

        state.setV(x, value & 0x00FF);
        state.setV(0xF, notBorrow);
    }

    /**
     * 8xy6 - SHR Vx {, Vy}
     * If the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0.
     * Then Vx is divided by 2.
     */
    private void shr(int x, int y) {
        int vf = (state.readV(x) & 0b1) == 1 ? 1 : 0;
        int value = state.readV(x) / 2;

        state.setV(x, value);
        state.setV(0xF, vf & 0x00FF);
    }

    /**
     * 8xyE - SHL Vx {, Vy}
     * If the most-significant bit of Vx is 1, then VF is set to 1, otherwise to 0.
     * Then Vx is multiplied by 2.
     */
    private void shl(int x, int y) {
        int vf = (state.readV(x) & 0b1) == 1 ? 1 : 0;
        int value = state.readV(x) * 2;

        state.setV(x, value);
        state.setV(0xF, vf & 0x00FF);
    }
}
