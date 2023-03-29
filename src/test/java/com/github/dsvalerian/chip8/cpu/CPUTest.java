package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.exception.NoProgramLoadedException;
import com.github.dsvalerian.chip8.io.Keyboard;
import com.github.dsvalerian.chip8.io.Screen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CPUTest {
    private static final String BLANK_STRING = "";
    private static final String TEST_ROM = "00 E0 00 E0 00 E0 00 E0 00 E0 00 E0";

    private CPUState state;
    private CPUTimings timings;
    private Screen screen;
    private Keyboard keyboard;
    private CPU cpu;

    @BeforeEach
    public void setUp() {
        state = new CPUState();
        timings = new CPUTimings(CPUSpeed.FULL, state);
        screen = new Screen();
        keyboard = new Keyboard();
        cpu = new CPU(state, timings, screen, keyboard);
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
