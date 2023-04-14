package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.exception.NoProgramLoadedException;
import com.github.dsvalerian.chip8.io.KeyState;
import com.github.dsvalerian.chip8.io.ScreenState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CPUTest {
    private static final String TEST_ROM = "00 E0 00 E0 00 E0 00 E0 00 E0 00 E0";

    private CPUState state;
    private ScreenState screenState;
    private CPU cpu;

    @BeforeEach
    public void setUp() {
        state = new CPUState();
        screenState = new ScreenState();
        cpu = new CPU(state, screenState);
    }

    @Test
    public void loadProgramTest() {
        Assertions.assertThrows(NoProgramLoadedException.class, () -> cpu.hasMoreInstructions());

        ROM rom = ROM.fromHexString(TEST_ROM);
        cpu.loadProgram(rom);
        Assertions.assertTrue(cpu.hasMoreInstructions());
    }

    @Test
    public void processInstructionsTest() {
        Assertions.assertThrows(NoProgramLoadedException.class, () -> cpu.processNextInstruction());

        ROM rom = ROM.fromHexString(TEST_ROM);
        cpu.loadProgram(rom);

        for (int i = 0; i < 6; i++) {
            Assertions.assertTrue(cpu.hasMoreInstructions());
            cpu.processNextInstruction();
        }

        Assertions.assertFalse(cpu.hasMoreInstructions());
    }
}
