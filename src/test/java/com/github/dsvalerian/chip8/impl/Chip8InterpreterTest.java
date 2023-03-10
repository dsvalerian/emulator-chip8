package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.Register;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Chip8InterpreterTest {
    private final Bits INSTRUCTION_BITS = Bits.SIXTEEN;
    private final int PC_STEP_SIZE = 2;

    private Chip8CPUState state;
    private Chip8Interpreter interpreter;
    private Register currentInstruction;

    @BeforeEach
    public void setUp() {
        state = new Chip8CPUState();
        interpreter = new Chip8Interpreter(state, PC_STEP_SIZE);
        currentInstruction = new Register(INSTRUCTION_BITS);

        Assertions.assertEquals(0x00, state.readPc());
    }

    @Test
    public void subroutineTest() {
        // CALL 0x455
        currentInstruction.set(0x2455);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // CALL 0xFFF
        currentInstruction.set(0x2FFF);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x0FFF, state.readPc());

        // RET
        currentInstruction.set(0x00EE);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // RET
        currentInstruction.set(0x00EE);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x0000, state.readPc());
    }

    @Test
    public void jumpTest() {
        // JP 0x150
        currentInstruction.set(0x1150);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x150, state.readPc());

        // LD V0, 0x25
        currentInstruction.set(0x6025);
        interpreter.executeInstruction(currentInstruction);

        // Bnnn - JP V0, addr
        currentInstruction.set(0xB123);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x123 + 0x25, state.readPc());
    }

    @Test
    public void basicLoadTest() {
        // LD V0, 0x25
        currentInstruction.set(0x6025);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x25, state.readV(0x0));
        Assertions.assertEquals(0x02, state.readPc());

        // LD V5, V0
        currentInstruction.set(0x8500);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x25, state.readV(0x5));
        Assertions.assertEquals(0x04, state.readPc());

        // LD [I], 0x123
        currentInstruction.set(0xA123);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x123, state.readI());
        Assertions.assertEquals(0x06, state.readPc());

        // LD DT, V5
        currentInstruction.set(0xF515);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x25, state.readDt());
        Assertions.assertEquals(0x08, state.readPc());

        // LD ST, V0
        currentInstruction.set(0xF018);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x25, state.readSt());
        Assertions.assertEquals(0x0A, state.readPc());

        // LD VA, DT
        currentInstruction.set(0xFA07);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x25, state.readV(0xA));
        Assertions.assertEquals(0x0C, state.readPc());
    }

    @Test
    public void loadBetweenRegistersAndIAddressTest() {
        // LD V0, 0x55
        currentInstruction.set(0x6055);
        interpreter.executeInstruction(currentInstruction);

        // LD V1, 0x55
        currentInstruction.set(0x6166);
        interpreter.executeInstruction(currentInstruction);

        // LD V2, 0x55
        currentInstruction.set(0x6277);
        interpreter.executeInstruction(currentInstruction);

        // LD I, 0x200
        currentInstruction.set(0xA200);
        interpreter.executeInstruction(currentInstruction);

        // LD [I], V2
        currentInstruction.set(0xF255);
        interpreter.executeInstruction(currentInstruction);

        // Checking that the address starting at [I] contains the V register values.
        Assertions.assertEquals(0x55, state.readMemory(state.readI() - 3));
        Assertions.assertEquals(0x66, state.readMemory(state.readI() - 2));
        Assertions.assertEquals(0x77, state.readMemory(state.readI() - 1));

        // I should now be equal to 0x203, which is blank data.

        // LD V2, [I]
        currentInstruction.set(0xF265);
        interpreter.executeInstruction(currentInstruction);

        // Checking that the registers are now zeroed out.
        Assertions.assertEquals(0x00, state.readV(0));
        Assertions.assertEquals(0x00, state.readV(1));
        Assertions.assertEquals(0x00, state.readV(2));
    }

    @Test
    public void addTest() {
        // LD V5, 0x55
        currentInstruction.set(0x6555);
        interpreter.executeInstruction(currentInstruction);

        // ADD V5, 0x11
        currentInstruction.set(0x7511);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x55 + 0x11, state.readV(0x5));

        // LD V6, 0x05
        currentInstruction.set(0x6605);
        interpreter.executeInstruction(currentInstruction);

        // ADD V5, V6
        currentInstruction.set(0x8564);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x55 + 0x11 + 0x05, state.readV(0x5));

        // LD I, 0x200
        currentInstruction.set(0xA200);
        interpreter.executeInstruction(currentInstruction);

        // Fx1E
        // ADD I, V5
        currentInstruction.set(0xF51E);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x55 + 0x11 + 0x05 + 0x200, state.readI());
    }

    @Test
    public void subtractTest() {
        // LD V0, 0x55
        currentInstruction.set(0x6055);
        interpreter.executeInstruction(currentInstruction);

        // LD V1, 0x11
        currentInstruction.set(0x6111);
        interpreter.executeInstruction(currentInstruction);

        // SUB V0, V1
        currentInstruction.set(0x8015);
        interpreter.executeInstruction(currentInstruction);

        Assertions.assertEquals(0x55 - 0x11, state.readV(0x0));
        Assertions.assertEquals(0x1, state.readV(0xF));

        // 8xy7 - SUBN Vx, Vy
        currentInstruction.set(0x8017);
        interpreter.executeInstruction(currentInstruction);

        Assertions.assertEquals(0x11 - 0x44 + 0x100, state.readV(0x0));
        Assertions.assertEquals(0x0, state.readV(0xF));
    }

    @Test
    public void binaryCompareTest() {
        // LD V5, 0x55
        currentInstruction.set(0x6555);
        interpreter.executeInstruction(currentInstruction);

        // LD V6, 0x11
        currentInstruction.set(0x6611);
        interpreter.executeInstruction(currentInstruction);

        // 8xy1
        currentInstruction.set(0x8561);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x55 | 0x11, state.readV(0x5));

        // 8xy2
        currentInstruction.set(0x8562);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals((0x55 | 0x11) & 0x11, state.readV(0x5));

        // 8xy3
        currentInstruction.set(0x8563);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(((0x55 | 0x11) & 0x11) ^ 0x11, state.readV(0x5));
    }

    @Test
    public void shiftTest() {
        // LD V0, 0b10011110
        currentInstruction.set(0x609E);
        interpreter.executeInstruction(currentInstruction);

        // SHR Vx {, Vy}
        currentInstruction.set(0x8106);
        interpreter.executeInstruction(currentInstruction);

        Assertions.assertEquals(0b01001111, state.readV(0x1));
        Assertions.assertEquals(0b0, state.readV(0xF));

        // 8xyE - SHL Vx {, Vy}
        currentInstruction.set(0x801E);
        interpreter.executeInstruction(currentInstruction);

        Assertions.assertEquals(0b10011110, state.readV(0x0));
        Assertions.assertEquals(0b0, state.readV(0xF));
    }

    @Test
    public void randTest() {
        // Cxkk - RND Vx, byte
        currentInstruction.set(0xC0FF);
        interpreter.executeInstruction(currentInstruction);
        System.out.println("Random number: " + state.readV(0));
    }

    @Test
    public void skipTest() {
        // 3xkk - SE Vx, byte
        currentInstruction.set(0x3000);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x4, state.readPc());

        // 5xy0 - SE Vx, Vy
        currentInstruction.set(0x5010);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x8, state.readPc());

        // 4xkk - SNE Vx, byte
        currentInstruction.set(0x4000);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0xA, state.readPc());

        // 9xy0 - SNE Vx, Vy
        currentInstruction.set(0x9010);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0xC, state.readPc());
    }

    // todo loading sprite
    // todo load decimal
    // todo load on keypress
}
