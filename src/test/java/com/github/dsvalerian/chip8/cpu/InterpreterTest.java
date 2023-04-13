package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.data.Sprites;
import com.github.dsvalerian.chip8.exception.UnsupportedInstructionException;
import com.github.dsvalerian.chip8.io.KeyState;
import com.github.dsvalerian.chip8.io.Pixel;
import com.github.dsvalerian.chip8.io.ScreenState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InterpreterTest {
    private static final KeyState KEY_STATE = KeyState.getInstance();
    private CPUState state;
    private ScreenState screenState;
    private Interpreter interpreter;
    private Register currentInstruction;

    @BeforeEach
    public void setUp() {
        state = new CPUState();
        screenState = new ScreenState();
        interpreter = new Interpreter(state, screenState);
        currentInstruction = new Register(Interpreter.INSTRUCTION_BITS);
        Sprites.load(state);
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
        // 3xkk - SE V0, 0x00
        currentInstruction.set(0x3000);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x4, state.readPc());

        // 5xy0 - SE V0, V1
        currentInstruction.set(0x5010);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x8, state.readPc());

        // 4xkk - SNE V0, 0x10
        currentInstruction.set(0x4010);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0xC, state.readPc());

        // 9xy0 - SNE V0, V1
        currentInstruction.set(0x9010);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0xE, state.readPc());

        // 6xkk - LD V0, 0x25
        currentInstruction.set(0x6025);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x10, state.readPc());

        // 9xy0 - SNE V0, V1
        currentInstruction.set(0x9010);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x14, state.readPc());
    }


    @Test
    public void advancedLoadTest() {
        // LD V0, 254
        currentInstruction.set(0x60FE);
        interpreter.executeInstruction(currentInstruction);

        // Fx33 - LD B, Vx
        currentInstruction.set(0xF033);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(2, state.readMemory(state.readI()));
        Assertions.assertEquals(5, state.readMemory(state.readI() + 1));
        Assertions.assertEquals(4, state.readMemory(state.readI() + 2));
    }

    @Test
    public void screenTest() {
        // Fx29 - LD F, V0
        currentInstruction.set(0xF029);
        interpreter.executeInstruction(currentInstruction);

        // Dxyn - DRW V0, V0, 5
        currentInstruction.set(0xD005);
        interpreter.executeInstruction(currentInstruction);

        // Verify a 4x5 zero is drawn to the screen.
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(0, 0));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(1, 0));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(2, 0));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(3, 0));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(0, 1));
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(1, 1));
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(2, 1));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(3, 1));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(0, 2));
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(1, 2));
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(2, 2));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(3, 2));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(0, 3));
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(1, 3));
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(2, 3));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(3, 3));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(0, 4));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(1, 4));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(2, 4));
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(3, 4));

        // 00E0 - CLS
        currentInstruction.set(0x00E0);
        interpreter.executeInstruction(currentInstruction);

        // Verify screen is clear.
        for (int i = 0; i < ScreenState.WIDTH; i++) {
            for (int j = 0; j < ScreenState.HEIGHT; j++) {
                Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(i, j));
            }
        }
    }

    @Test
    public void sysTest() {
        // 0nnn - SYS 123
        currentInstruction.set(0x0123);
        interpreter.executeInstruction(currentInstruction);

        // Verify PC was incremented.
        Assertions.assertEquals(2, state.readPc());
    }

    @Test
    public void keyPressTest() {
        KEY_STATE.press(0);

        currentInstruction.set(0xE09E);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x4, state.readPc());

        currentInstruction.set(0xE1A1);
        interpreter.executeInstruction(currentInstruction);
        Assertions.assertEquals(0x8, state.readPc());

        // Fx0A - LD Vx, K
        currentInstruction.set(0xF50A);
        interpreter.executeInstruction(currentInstruction);

        // Press a key to resume execution.
        KEY_STATE.press(10);
        Assertions.assertEquals(10, state.readV(5));

    }

    @Test
    public void unsupportedInstructionsTest() {
        currentInstruction.set(0x5001);
        Assertions.assertThrows(UnsupportedInstructionException.class, () ->
            interpreter.executeInstruction(currentInstruction));

        currentInstruction.set(0x800F);
        Assertions.assertThrows(UnsupportedInstructionException.class, () ->
                interpreter.executeInstruction(currentInstruction));

        currentInstruction.set(0x9001);
        Assertions.assertThrows(UnsupportedInstructionException.class, () ->
                interpreter.executeInstruction(currentInstruction));

        currentInstruction.set(0xE09F);
        Assertions.assertThrows(UnsupportedInstructionException.class, () ->
                interpreter.executeInstruction(currentInstruction));

        currentInstruction.set(0xF066);
        Assertions.assertThrows(UnsupportedInstructionException.class, () ->
                interpreter.executeInstruction(currentInstruction));
    }

    // todo loading sprite
    // todo load decimal
    // todo load on keypress
}
