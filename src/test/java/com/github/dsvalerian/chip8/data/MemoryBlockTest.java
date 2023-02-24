package com.github.dsvalerian.chip8.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemoryBlockTest {
    private final int MEMORY_SIZE = 0x1000;

    private MemoryBlock memory;

    @BeforeEach
    public void setUp() {
        memory = new MemoryBlock(MEMORY_SIZE, Bits.EIGHT);
    }

    @Test
    public void basicGetterSetterTest() {
        memory.set(0xFF, 0x12);

        Assertions.assertEquals(0x12, memory.read(0xFF));
    }

    @Test
    public void outOfBoundsTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.read(0x1001));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.read(0x1000));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.read(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x1001, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x1000, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(-1, 0x0));
        Assertions.assertDoesNotThrow(() -> memory.set(0x0, 0x0));
        Assertions.assertDoesNotThrow(() -> memory.read(0x0));
    }

    @Test
    public void emptyMemoryTest() {
        for (int i = 0; i < memory.getSize(); i++) {
            Assertions.assertEquals(0x0, memory.read(i));
        }
    }

    @Test
    public void invalidValueSetTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x10, 256));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x10, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> memory.set(0x10, 0xFFFF));
    }

    @Test
    public void getUsingRegisterTest() {
        Register register = new Register(Bits.SIXTEEN);
        register.set(512);
        memory.set(512, 100);

        Assertions.assertEquals(100, memory.read(register));
    }

    @Test
    public void setUsingRegisterTest() {
        Register address = new Register(Bits.SIXTEEN);
        address.set(1024);
        Register value = new Register(Bits.EIGHT);
        value.set(100);
        memory.set(512, value);
        memory.set(address, 255);

        Assertions.assertEquals(100, memory.read(512));
        Assertions.assertEquals(255, memory.read(1024));

        memory.set(address, value);

        Assertions.assertEquals(100, memory.read(1024));
    }
}
