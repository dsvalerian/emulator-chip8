package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPUState;
import com.github.dsvalerian.chip8.Interpreter;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.IllegalInstructionException;
import com.github.dsvalerian.chip8.util.Constants;

/**
 * @inheritDocs
 *
 * The default implementation of {@link Interpreter}.
 */
public class Chip8Interpreter implements Interpreter {
    private Chip8CPUState state;

    /**
     * Constructs a {@link Chip8Interpreter} with an assigned {@link CPUState}.
     *
     * @param state The {@link CPUState} that will be used when processing instructions.
     */
    public Chip8Interpreter(CPUState state) {
        this.state = (Chip8CPUState)state;
    }

    /**
     * @inheritDoc
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
                    case 0x0E0:
                        // 00E0 - Clear the display.
                        cls();
                        break;
                    case 0x0EE:
                        // 00EE - Return from subroutine.
                        ret();
                        break;
                    default:
                        // 0nnn - Jump to machine code at address nnn.
                        sys(instruction & 0x0FFF);
                        break;
                }
                break;
            case 0x1000:
                // 1nnn - Jump to address nnn.
                jp(instruction & 0x0FFF);
                break;
            case 0x2000:
                // 2nnn - Call subroutine at address nnn.
                call(instruction & 0x0FFF);
                break;
            case 0x3000:
                // 3xkk - Skip next instruction if Vx == kk
                seByte(instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x4000:
                // 4xkk - Skip next instruction if Vx != kk
                sneByte(instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x5000:
                switch (instruction & 0x000F) {
                    case 0x0:
                        // 5xy0 - Skip next instruction if Vx == Vy
                        seRegister(instruction & 0x0F00, instruction & 0x00F0);
                        break;
                    default:
                        throw new IllegalInstructionException(instruction);
                }
            case 0x6000:
                // 6xkk - Set Vx = kk
                ldByte(instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x7000:
                // 7xkk - Set Vx = Vx + kk
                addByte(instruction & 0x0F00, instruction & 0x00FF);
                break;
            case 0x9000:
                switch (instruction & 0x000F) {
                    case 0x0:
                        // 9xy0 - Skip next instruction if Vx == Vy
                        sneRegister(instruction & 0x0F00, instruction & 0x00F0);
                        break;
                    default:
                        throw new IllegalInstructionException(instruction);
                }
            default:
                throw new IllegalInstructionException(instruction);
        }
    }

    private void sys(int address) {
        // Apparently this should be ignored by modern interpreters. Do nothing.
    }

    private void cls() {
        // todo clear the screen
    }

    private void ret() {
        state.setPc(state.popStack());
    }

    private void jp(int address) {
        state.setPc(address);
    }

    private void call(int address) {
        state.pushStack(state.readPc());
        state.setPc(address);
    }

    private void seByte(int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) == kk) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private void seRegister(int x, int y) {
        // skip instruction if register Vx == Vy
        if (state.readV(x) == state.readV(y)) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private void sneByte(int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) != kk) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private void sneRegister(int x, int y) {
        // skip instruction if register Vx != Vy
        if (state.readV(x) != state.readV(y)) {
            state.setPc(state.readPc() + Constants.CHIP8_PC_STEP_SIZE);
        }
    }

    private void ldByte(int x, int kk) {
        state.setV(x, kk);
    }

    private void addByte(int x, int kk) {
        state.setV(x, (state.readV(x) + kk) & 0x00FF);
    }
}
