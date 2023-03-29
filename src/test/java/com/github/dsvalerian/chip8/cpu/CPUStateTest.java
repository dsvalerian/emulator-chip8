package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.cpu.CPUState;
import com.github.dsvalerian.chip8.exception.StackEmptyException;
import com.github.dsvalerian.chip8.exception.StackFullException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CPUStateTest {
    private CPUState state;

    @BeforeEach
    public void setUp() {
        state = new CPUState();
    }

    @Test
    public void setReadMemoryTest() {
        state.setMemory(0xFFF, 42);
        Assertions.assertEquals(42, state.readMemory(0xFFF));

        Assertions.assertThrows(IllegalArgumentException.class, () -> state.readMemory(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> state.readMemory(0x1000));
    }

    @Test
    public void setReadVTest() {
        state.setV(15, 42);
        Assertions.assertEquals(42, state.readV(15));

        Assertions.assertThrows(IllegalArgumentException.class, () -> state.readV(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> state.readV(16));
    }

    @Test
    public void setReadITest() {
        state.setI(0x42);
        Assertions.assertEquals(0x42, state.readI());

        Assertions.assertThrows(IllegalArgumentException.class, () -> state.setI(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> state.setI(0x1000));
    }

    @Test
    public void setReadPCTest() {
        state.setPc(0xFFFF);
        Assertions.assertEquals(0xFFFF, state.readPc());

        Assertions.assertThrows(IllegalArgumentException.class, () -> state.setPc(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> state.setPc(0x10000));
    }

    @Test
    public void pushPopStackTest() {
        Assertions.assertThrows(StackEmptyException.class, () -> state.popStack());

        int[] values = {0x42, 0xFFFF, 0x00, 0x9B};

        for (int i = 0; i < values.length; i++) {
            state.pushStack(values[i]);
        }

        for (int i = values.length - 1; i >= 0; i--) {
            Assertions.assertEquals(values[i], state.popStack());
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> state.pushStack(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> state.setPc(0x10000));

        Assertions.assertThrows(StackFullException.class, () -> {
           for (int i = 0; i < CPUState.STACK_SIZE + 1; i++) {
               state.pushStack(0);
           }
        });
    }
}
