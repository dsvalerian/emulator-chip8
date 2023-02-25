package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstructionsTest {
    private CPUState state;

    @BeforeEach
    public void setUp() {
        state = new CPUState(CPUProfile.CHIP8);
    }

    @Test
    public void subroutineTest() {
        Assertions.assertEquals(0x00, state.readPc());
        Register instruction = new Register(Constants.CHIP8_INSTRUCTION_BITS);

        // CALL 0x455
        instruction.set(0x2455);
        Instructions.execute(state, instruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // CALL 0xFFF
        instruction.set(0x2FFF);
        Instructions.execute(state, instruction);
        Assertions.assertEquals(0x0FFF, state.readPc());

        // RET
        instruction.set(0x00EE);
        Instructions.execute(state, instruction);
        Assertions.assertEquals(0x0455, state.readPc());

        // RET
        instruction.set(0x00EE);
        Instructions.execute(state, instruction);
        Assertions.assertEquals(0x0000, state.readPc());
    }

    @Test
    public void jumpTest() {
        Assertions.assertEquals(0x00, state.readPc());
        Register instruction = new Register(Constants.CHIP8_INSTRUCTION_BITS);

        // JP 0x150
        instruction.set(0x1150);
        Instructions.execute(state, instruction);
        Assertions.assertEquals(0x150, state.readPc());
    }
}
