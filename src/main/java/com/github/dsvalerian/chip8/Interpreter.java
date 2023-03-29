package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.data.Sprites;
import com.github.dsvalerian.chip8.exception.UnsupportedInstructionException;
import com.github.dsvalerian.chip8.io.Keyboard;
import com.github.dsvalerian.chip8.io.Screen;

import java.util.Random;

/**
 * Handles Chip-8 instruction processing.
 */
public class Interpreter {
    /**
     * The number of bits each instruction uses.
     */
    public static final Bits INSTRUCTION_BITS = Bits.SIXTEEN;
    /**
     * The size of steps the program counter makes to get to the next instruction.
     */
    public static final int PC_STEP_SIZE = 2;

    private final int MAX_V_REGISTER_VALUE = 1 << CPUState.V_REGISTER_SIZE.getValue();

    private CPUState state;
    private Screen screen;
    private Keyboard keyboard;
    private Random random;

    /**
     * Constructs a {@link Interpreter} with an assigned {@link CPUState}.
     *
     * @param state The {@link CPUState} that will be used when processing instructions.
     * @param screen The {@link Screen} that will be drawn to when processing instructions.
     * @param keyboard The {@link Keyboard} that is checked when processing instructions.
     */
    public Interpreter(CPUState state, Screen screen, Keyboard keyboard) {
        this.state = state;
        this.screen = screen;
        this.keyboard = keyboard;
        random = new Random();
    }

    /**
     * Decode and execute a Chip-8 instruction stored in a {@link Register}.
     *
     * @param instruction A {@link Register} holding the instruction as a 16-bit value.
     */
    public void executeInstruction(Register instruction) {
        executeInstruction(instruction.read());
    }

    private void executeInstruction(int instruction) {
        // Decode the instruction by parts and delegate work to an instruction method.
        switch (instruction & 0xF000) {
            case 0x0000:
                switch(instruction & 0x0FFF) {
                    // 00E0
                    case 0x0E0: clearScreen(); break;
                    // 00EE
                    case 0x0EE: ret(); break;
                    // 0nnn
                    default: sys(getNnn(instruction)); break;
                }
                break;
            // 1nnn
            case 0x1000: jumpNnn(getNnn(instruction)); break;
            // 2nnn
            case 0x2000: callNnn(getNnn(instruction)); break;
            // 3xkk
            case 0x3000: skipIfVxEqualKk(getX(instruction), getKk(instruction)); break;
            // 4xkk
            case 0x4000: skipIfVxNotEqualKk(getX(instruction), getKk(instruction)); break;
            case 0x5000:
                switch (instruction & 0x000F) {
                    // 5xy0
                    case 0x0: skipIfVxEqualVy(getX(instruction), getY(instruction)); break;
                    default: throw new UnsupportedInstructionException(instruction);
                }
                break;
            // 6xkk
            case 0x6000: loadByteIntoVx(getX(instruction), getKk(instruction)); break;
            // 7xkk
            case 0x7000: addKkToVx(getX(instruction), getKk(instruction)); break;
            case 0x8000:
                switch (instruction & 0x000F) {
                    // 8xy0
                    case 0x0: loadVyIntoVx(getX(instruction), getY(instruction)); break;
                    // 8xy1
                    case 0x1: or(getX(instruction), getY(instruction)); break;
                    // 8xy2
                    case 0x2: and(getX(instruction), getY(instruction)); break;
                    // 8xy3
                    case 0x3: xor(getX(instruction), getY(instruction)); break;
                    // 8xy4
                    case 0x4: addVyToVx(getX(instruction), getY(instruction)); break;
                    // 8xy5
                    case 0x5: subtractVyFromVx(getX(instruction), getY(instruction)); break;
                    // 8xy6
                    case 0x6: shr(getX(instruction), getY(instruction)); break;
                    // 8xy7
                    case 0x7: subtractVxFromVy(getX(instruction), getY(instruction)); break;
                    // 8xyE
                    case 0xE: shl(getX(instruction), getY(instruction)); break;
                    default: throw new UnsupportedInstructionException(instruction);
                }
                break;
            case 0x9000:
                switch (instruction & 0x000F) {
                    // 9xy0
                    case 0x0: skipIfVxNotEqualVy(getX(instruction), getY(instruction)); break;
                    default: throw new UnsupportedInstructionException(instruction);
                }
                break;
            // Annn
            case 0xA000: loadNnnIntoI(getNnn(instruction)); break;
            // Bnnn
            case 0xB000: jumpV0PlusNnn(getNnn(instruction)); break;
            // Cxkk
            case 0xC000: rand(getX(instruction), getKk(instruction)); break;
            // Dxyn
            case 0xD000: draw(getX(instruction), getY(instruction), getNibble(instruction)); break;
            case 0xE000:
                switch (instruction & 0x00FF) {
                    // Ex9E
                    case 0x9E: skipIfKeyPressed(getX(instruction)); break;
                    // ExA1
                    case 0xA1: skipIfKeyNotPressed(getX(instruction)); break;
                    default: throw new UnsupportedInstructionException(instruction);
                }
                break;
            case 0xF000:
                switch (instruction & 0x00FF) {
                    // Fx07
                    case 0x07: loadDtIntoX(getX(instruction)); break;
                    // Fx0A
                    case 0x0A: loadOnKeyPress(getX(instruction)); break;
                    // Fx15
                    case 0x15: loadXIntoDt(getX(instruction)); break;
                    // Fx18
                    case 0x18: loadXIntoSt(getX(instruction)); break;
                    // Fx1E
                    case 0x1E: addVxToI(getX(instruction)); break;
                    // Fx29
                    case 0x29: loadSpriteIntoI(getX(instruction)); break;
                    // Fx33
                    case 0x33: loadDecimalIntoI(getX(instruction)); break;
                    // Fx55
                    case 0x55: loadVRegistersIntoIAddress(getX(instruction)); break;
                    // Fx65
                    case 0x65: loadIBlockIntoVRegisters(getX(instruction)); break;
                    default: throw new UnsupportedInstructionException(instruction);
                }
                break;
            default: throw new UnsupportedInstructionException(instruction);
        }
    }

    private void incrementPc() {
        state.setPc(state.readPc() + PC_STEP_SIZE);
    }

    /**
     * Get the x value, aka the second nibble, from an instruction.
     *
     * @param instruction The instruction to get the x value from.
     * @return The second nibble in the instruction.
     */
    private int getX(int instruction) {
        return (instruction & 0x0F00) >> 8;
    }

    /**
     * Get the y value, aka the third nibble, from an instruction.
     *
     * @param instruction The instruction to get the y value from.
     * @return The third nibble in the instruction.
     */
    private int getY(int instruction) {
        return (instruction & 0x00F0) >> 4;
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
     * Get the n value, aka the last nibble, from an instruction.
     *
     * @param instruction The instruction to get the n value from.
     * @return The last nibble in the instruction.
     */
    private int getNibble(int instruction) {
        return instruction & 0x000F;
    }

    /**
     * 0nnn - SYS addr
     * This instruction is only used on the old computers on which Chip-8 was originally implemented.
     * It is ignored by modern interpreters.
     */
    private void sys(int address) {
        // Ignore.
        incrementPc();
    }

    /**
     * 00E0 - CLS
     * Clear the display.
     */
    private void clearScreen() {
        screen.clear();

        incrementPc();
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
    private void jumpNnn(int address) {
        state.setPc(address);
    }

    /**
     * 2nnn - CALL addr
     * The interpreter increments the stack pointer, then puts the current PC on the top of the stack.
     * The PC is then set to nnn.
     */
    private void callNnn(int address) {
        state.pushStack(state.readPc());
        state.setPc(address);
    }

    /**
     * 3xkk - SE Vx, byte
     * The interpreter compares register Vx to kk, and if they are equal,
     * increments the program counter by 2.
     */
    private void skipIfVxEqualKk(int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) == kk) {
            incrementPc();
        }

        incrementPc();
    }

    /**
     * 5xy0 - SE Vx, Vy
     * The interpreter compares register Vx to register Vy, and if they are equal,
     * increments the program counter by 2.
     */
    private void skipIfVxEqualVy(int x, int y) {
        // skip instruction if register Vx == Vy
        if (state.readV(x) == state.readV(y)) {
            incrementPc();
        }

        incrementPc();
    }

    /**
     * 4xkk - SNE Vx, byte
     * The interpreter compares register Vx to kk, and if they are not equal,
     * increments the program counter by 2.
     */
    private void skipIfVxNotEqualKk(int x, int kk) {
        // skip instruction if register Vx == kk
        if (state.readV(x) != kk) {
            incrementPc();
        }

        incrementPc();
    }

    /**
     * 9xy0 - SNE Vx, Vy
     * The values of Vx and Vy are compared, and if they are not equal,
     * the program counter is increased by 2.
     */
    private void skipIfVxNotEqualVy(int x, int y) {
        // skip instruction if register Vx != Vy
        if (state.readV(x) != state.readV(y)) {
            incrementPc();
        }

        incrementPc();
    }

    /**
     * Ex9E - SKP Vx
     * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2.
     */
    private void skipIfKeyPressed(int x) {
        if (keyboard.isPressed(x)) {
            incrementPc();
        }

        incrementPc();
    }

    /**
     * ExA1 - SKNP Vx
     * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the up position, PC is increased by 2.
     */
    private void skipIfKeyNotPressed(int x) {
        if (!keyboard.isPressed(x)) {
            incrementPc();
        }

        incrementPc();
    }

    /**
     * 6xkk - LD Vx, byte
     * The interpreter puts the value kk into register Vx.
     */
    private void loadByteIntoVx(int x, int kk) {
        state.setV(x, kk);

        incrementPc();
    }

    /**
     * 8xy0 - LD Vx, Vy
     * Stores the value of register Vy in register Vx.
     */
    private void loadVyIntoVx(int x, int y) {
        state.setV(x, state.readV(y));

        incrementPc();
    }

    /**
     * Annn - LD I, addr
     * The value of register I is set to nnn.
     */
    private void loadNnnIntoI(int nnn) {
        state.setI(nnn);

        incrementPc();
    }

    /**
     * 7xkk - ADD Vx, byte
     * Adds the value kk to the value of register Vx, then stores the result in Vx.
     */
    private void addKkToVx(int x, int kk) {
        int value = state.readV(x) + kk;
        state.setV(x, value & 0x00FF);

        incrementPc();
    }

    /**
     * 8xy4 - ADD Vx, Vy
     * The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., > 255,)
     * VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored in Vx.
     */
    private void addVyToVx(int x, int y) {
        int value = state.readV(x) + state.readV(y);
        int carry = value > 0x00FF ? 1 : 0;

        state.setV(x, value & 0x00FF);
        state.setV(0xF, carry);

        incrementPc();
    }

    /**
     * 8xy1 - OR Vx, Vy
     * Performs a bitwise OR on the values of Vx and Vy, then stores the result in Vx.
     * A bitwise OR compares the corrseponding bits from two values, and if either bit is 1,
     * then the same bit in the result is also 1. Otherwise, it is 0.
     */
    private void or(int x, int y) {
        state.setV(x, state.readV(x) | state.readV(y));

        incrementPc();
    }

    /**
     * 8xy2 - AND Vx, Vy
     * Performs a bitwise AND on the values of Vx and Vy, then stores the result in Vx.
     * A bitwise AND compares the corrseponding bits from two values, and if both bits are 1,
     * then the same bit in the result is also 1. Otherwise, it is 0.
     */
    private void and(int x, int y) {
        state.setV(x, state.readV(x) & state.readV(y));

        incrementPc();
    }

    /**
     * 8xy3 - XOR Vx, Vy
     * Performs a bitwise exclusive OR on the values of Vx and Vy, then stores the result in Vx.
     * An exclusive OR compares the corrseponding bits from two values, and if the bits are not both the same,
     * then the corresponding bit in the result is set to 1. Otherwise, it is 0.
     */
    private void xor(int x, int y) {
        state.setV(x, state.readV(x) ^ state.readV(y));

        incrementPc();
    }

    /**
     * 8xy5 - SUB Vx, Vy
     * If Vx > Vy, then VF is set to 1, otherwise 0. Then Vy is subtracted from Vx,
     * and the results stored in Vx.
     */
    private void subtractVyFromVx(int x, int y) {
        state.setV(0xF, state.readV(x) > state.readV(y) ? 1 : 0);

        // if newValue is negative, add it to the max value (so it wraps around, essentially)
        int newValue = state.readV(x) - state.readV(y);
        newValue = newValue < 0 ? MAX_V_REGISTER_VALUE + newValue : newValue;

        state.setV(x, newValue);

        incrementPc();
    }

    /**
     * 8xy7 - SUBN Vx, Vy
     * If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy,
     * and the results stored in Vx.
     */
    private void subtractVxFromVy(int x, int y) {
        state.setV(0xF, state.readV(x) < state.readV(y) ? 1 : 0);

        // if newValue is negative, add it to the max value (so it wraps around, essentially)
        int newValue = state.readV(y) - state.readV(x);
        newValue = newValue < 0 ? MAX_V_REGISTER_VALUE + newValue : newValue;

        state.setV(x, newValue);

        incrementPc();
    }

    /**
     * 8xy6 - SHR Vx {, Vy}
     * Store the value of register VY shifted right one bit in register VX
     * Set register VF to the least significant bit prior to the shift
     * VY is unchanged
     */
    private void shr(int x, int y) {
        state.setV(x, state.readV(y) >> 1);
        state.setV(0xF, state.readV(y) & 0b1);

        incrementPc();
    }

    /**
     * 8xyE - SHL Vx {, Vy}
     * Store the value of register VY shifted left one bit in register VX
     * Set register VF to the most significant bit prior to the shift
     * VY is unchanged
     */
    private void shl(int x, int y) {
        state.setV(x, state.readV(y) << 1);
        state.setV(0xF, (state.readV(y) & 0b10000000) >> 7);

        incrementPc();
    }

    /**
     * Bnnn - JP V0, addr
     * The program counter is set to nnn plus the value of V0.
     */
    private void jumpV0PlusNnn(int nnn) {
        state.setPc(state.readV(0x0) + nnn);
    }

    /**
     * Cxkk - RND Vx, byte
     * Generates a random number from 0 to 255, which is then ANDed with the value kk. The results are stored in Vx.
     */
    private void rand(int x, int kk) {
        state.setV(x, kk & random.nextInt(256));

        incrementPc();
    }

    /**
     * Dxyn - DRW Vx, Vy, n
     * The interpreter reads n bytes from memory, starting at the address stored in I.
     * These bytes are then displayed as sprites on screen at coordinates (Vx, Vy). Sprites are XORed
     * onto the existing screen. If this causes any pixels to be erased, VF is set to 1, otherwise it is set to 0.
     * If the sprite is positioned so part of it is outside the coordinates of the display,
     * it wraps around to the opposite side of the screen. See instruction 8xy3 for more information on XOR,
     * and section 2.4, Display, for more information on the Chip-8 screen and sprites.
     */
    private void draw(int x, int y, int n) {
        boolean pixelsDeactivated = false;
        System.out.println(String.format("x: %d, y: %d, n: %d", x, y, n));

        for (int i = 0; i < n; i++) {
            int spriteRow = state.readMemory(state.readI() + i);

            // Split and draw each bit in the row as a sprite.
            for (int j = 0; j < 8; j++) {
                int shiftAmount = 8 - j - 1;
                int currentBit = (spriteRow & (1 << shiftAmount)) >> shiftAmount;
                int xCoordinate = (state.readV(x) + j) % Screen.WIDTH;
                int yCoordinate = (state.readV(y) + i) % Screen.HEIGHT;
                int oldPixel = screen.readPixel(xCoordinate, yCoordinate) == true ? 1 : 0;
                int newPixel = oldPixel ^ currentBit;

                screen.setPixel(xCoordinate, yCoordinate, newPixel == 1 ? true : false);
                pixelsDeactivated = oldPixel == 1 && newPixel == 0 ? true : pixelsDeactivated;
            }
        }

        state.setV(0xF, pixelsDeactivated ? 1 : 0);

        incrementPc();
    }

    /**
     * Fx07 - LD Vx, DT
     * The value of DT is placed into Vx.
     */
    private void loadDtIntoX(int x) {
        state.setV(x, state.readDt());

        incrementPc();
    }

    /**
     * Fx0A - LD Vx, K
     * All execution stops until a key is pressed, then the value of that key is stored in Vx.
     */
    private void loadOnKeyPress(int x) {
        state.pause();

        keyboard.setOnNextKeyPress((lastKeyPressed) -> {
            // Finish the LD Vx, K instruction.
            state.setV(x, lastKeyPressed);
            incrementPc();

            state.resume();
        });
    }

    /**
     * Fx15 - LD DT, Vx
     * DT is set equal to the value of Vx.
     */
    private void loadXIntoDt(int x) {
        state.setDt(state.readV(x));

        incrementPc();
    }

     /**
     * Fx18 - LD ST, Vx
     * ST is set equal to the value of Vx.
     */
     private void loadXIntoSt(int x) {
         state.setSt(state.readV(x));

         incrementPc();
     }

     /**
     * Fx1E - ADD I, Vx
     * The values of I and Vx are added, and the results are stored in I.
     */
     private void addVxToI(int x) {
         state.setI(state.readI() + state.readV(x));

         incrementPc();
     }

     /**
     * Fx29 - LD F, Vx
     * The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx.
     */
     private void loadSpriteIntoI(int x) {
         state.setI(Sprites.lookUp(state.readV(x) & 0xF));

         incrementPc();
     }

     /**
     * Fx33 - LD B, Vx
     * The interpreter takes the decimal value of Vx, and places the hundreds digit in memory at location in I,
      * the tens digit at location I+1, and the ones digit at location I+2.
     */
     private void loadDecimalIntoI(int x) {
         int value =  state.readV(x);
         int hundredsDigit = value / 100;
         int tensDigit = (value % 100) / 10;
         int onesDigit = value % 10;

         state.setMemory(state.readI(), hundredsDigit);
         state.setMemory(state.readI() + 1, tensDigit);
         state.setMemory(state.readI() + 2, onesDigit);

         incrementPc();
     }

     /**
     * Fx55 - LD [I], Vx
     * The interpreter copies the values of registers V0 through Vx into memory, starting at the address in I.
     */
     private void loadVRegistersIntoIAddress(int x) {
         for (int i = 0; i <= x; i++) {
             state.setMemory(state.readI() + i, state.readV(i));
         }

         state.setI(state.readI() + x + 1);
         incrementPc();
     }

     /**
     * Fx65 - LD Vx, [I]
     * The interpreter reads values from memory starting at location I into registers V0 through Vx.
     */
     private void loadIBlockIntoVRegisters(int x) {
         for (int i = 0; i <= x; i++) {
             state.setV(i, state.readMemory(state.readI() + i));
         }

         state.setI(state.readI() + x + 1);
         incrementPc();
     }
}
