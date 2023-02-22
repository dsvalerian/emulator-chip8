package com.github.dsvalerian.chip8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistersTest {
    private Registers registers;

    @BeforeEach
    public void setUp() {
        registers = new Registers();
    }

    @Test
    public void basicGetterSetterTest() {
        registers.setV(0xF, 0x12);
        registers.setI(0x55);

        Assertions.assertEquals(0x12, registers.getV(0xF));
        Assertions.assertEquals(0x55, registers.getI());
    }

    @Test
    public void outOfBoundsTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.getV(0x10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.getV(0xFF));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.getV(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setV(0x10, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setV(0xFF, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setV(-1, 0x0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setI(0xFFFF));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setI(-1));
    }

    @Test
    public void emptyMemoryTest() {
        for (int i = 0; i < Registers.NUM_REGISTERS; i++) {
            Assertions.assertEquals(0x0, registers.getV(i));
        }
    }

    @Test
    public void invalidValueSetTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setV(0xA, 256));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setV(0xA, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> registers.setV(0xA, 0xFFFF));
    }
}
