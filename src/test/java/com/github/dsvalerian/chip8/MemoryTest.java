package com.github.dsvalerian.chip8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemoryTest {
    private Memory memory;

    @BeforeEach
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void basicGetterSetterTest() {
        memory.set(0xFF, 0x12);

        Assertions.assertEquals(0x12, memory.get(0xFF));
    }

    @Test
    public void outOfBoundsTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.get(0x1001));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.get(0x1000));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.get(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x1001, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x1000, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(-1, 0x0));
        Assertions.assertDoesNotThrow(() -> memory.set(0x0, 0x0));
        Assertions.assertDoesNotThrow(() -> memory.get(0x0));
    }

    @Test
    public void emptyMemoryTest() {
        for (int i = 0; i < Memory.SIZE; i++) {
            Assertions.assertEquals(0x0, memory.get(i));
        }
    }

    @Test
    public void invalidValueSetTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x10, 256));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x10, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x10, 0xFFFF));
    }
}
