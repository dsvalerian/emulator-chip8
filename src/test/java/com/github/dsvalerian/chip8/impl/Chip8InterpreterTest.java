package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPUProfile;
import com.github.dsvalerian.chip8.data.Register;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Chip8InterpreterTest {
    private final CPUProfile PROFILE = CPUProfile.CHIP8;

    private Chip8CPUState state;
    private Chip8Interpreter instructions;

    @BeforeEach
    public void setUp() {
        state = new Chip8CPUState();
        instructions = new Chip8Interpreter(state);
    }

    @Test
    public void subroutineTest() {
        Assertions.assertEquals(0x00, state.readPc());
        Register instruction = new Register(PROFILE.getInstructionBits());

        // CALL 0x455
        instruction.set(0x2455);
        instructions.executeInstruction(instruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // CALL 0xFFF
        instruction.set(0x2FFF);
        instructions.executeInstruction(instruction);
        Assertions.assertEquals(0x0FFF, state.readPc());

        // RET
        instruction.set(0x00EE);
        instructions.executeInstruction(instruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // RET
        instruction.set(0x00EE);
        instructions.executeInstruction(instruction);
        Assertions.assertEquals(0x0000, state.readPc());
    }

    @Test
    public void jumpTest() {
        Assertions.assertEquals(0x00, state.readPc());
        Register instruction = new Register(PROFILE.getInstructionBits());

        // JP 0x150
        instruction.set(0x1150);
        instructions.executeInstruction(instruction);
        Assertions.assertEquals(0x150, state.readPc());
    }
}
