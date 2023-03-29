package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.ROMTest;
import com.github.dsvalerian.chip8.io.Keyboard;
import com.github.dsvalerian.chip8.io.Screen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class CPUTest {
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
    public void testSuiteIBMScreenTest() {
        ROM rom = getRom("chip8-test-suite.ch8");

        // Enable the splash screen test
        state.setMemory(0x1FF, 1);

        cpu.loadProgram(rom);
        for (int i = 0; i < 250; i++) {
            cpu.processNextInstruction();
        }

        System.out.println(screen);
    }

    private ROM getRom(String filename) {
        URL romTestResource = ROMTest.class.getClassLoader().getResource(filename);
        ROM rom = null;

        try {
            rom = ROM.fromFile(Paths.get(romTestResource.toURI()).toString());
        }
        catch (URISyntaxException ex) {
            ex.printStackTrace();
            Assertions.fail("URISyntaxException when getting resource file path");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            Assertions.fail("IOException when reading file");
        }

        return rom;
    }
}
