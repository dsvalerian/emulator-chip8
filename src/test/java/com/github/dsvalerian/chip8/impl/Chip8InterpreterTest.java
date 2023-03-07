package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPUProfile;
import com.github.dsvalerian.chip8.data.Register;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Chip8InterpreterTest {
    private final CPUProfile PROFILE = CPUProfile.CHIP8;

    private Chip8CPUState state;
    private Chip8Interpreter interpreter;

    @BeforeEach
    public void setUp() {
        state = new Chip8CPUState();
        interpreter = new Chip8Interpreter(state);
    }

    @Test
    public void subroutineTest() {
        Assertions.assertEquals(0x00, state.readPc());
        Register instruction = new Register(PROFILE.getInstructionBits());

        // CALL 0x455
        instruction.set(0x2455);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // CALL 0xFFF
        instruction.set(0x2FFF);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x0FFF, state.readPc());

        // RET
        instruction.set(0x00EE);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // RET
        instruction.set(0x00EE);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x0000, state.readPc());
    }

    @Test
    public void jumpTest() {
        Assertions.assertEquals(0x00, state.readPc());
        Register instruction = new Register(PROFILE.getInstructionBits());

        // JP 0x150
        instruction.set(0x1150);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x150, state.readPc());
    }

    @Test
    public void basicLoadTest() {
        Register instruction = new Register(PROFILE.getInstructionBits());
        Assertions.assertEquals(0x00, state.readPc());

        // LD V0, 0x25
        instruction.set(0x6025);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x25, state.readV(0x0));
        Assertions.assertEquals(0x02, state.readPc());

        // LD V5, V0
        instruction.set(0x8500);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x25, state.readV(0x5));
        Assertions.assertEquals(0x04, state.readPc());

        // LD [I], 0x123
        instruction.set(0xA123);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x123, state.readI());
        Assertions.assertEquals(0x06, state.readPc());

        // LD DT, V5
        instruction.set(0xF515);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x25, state.readDt());
        Assertions.assertEquals(0x08, state.readPc());

        // LD ST, V0
        instruction.set(0xF018);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x25, state.readSt());
        Assertions.assertEquals(0x0A, state.readPc());

        // LD VA, DT
        instruction.set(0xFA07);
        interpreter.executeInstruction(instruction);
        Assertions.assertEquals(0x25, state.readV(0xA));
        Assertions.assertEquals(0x0C, state.readPc());
    }
}
