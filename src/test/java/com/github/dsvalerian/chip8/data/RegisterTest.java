package com.github.dsvalerian.chip8.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTest {
    private Register register;

    @Test
    public void constructorTest() {
        register = new Register(Bits.EIGHT);
        Assertions.assertEquals(0, register.read());
    }

    @Test
    public void setterGetterTest() {
        register = new Register(Bits.EIGHT);
        register.set(0xFF);
        Assertions.assertEquals(0xFF, register.read());

        Register newRegister = new Register(Bits.EIGHT);
        newRegister.set(0x52);
        register.set(newRegister);
        Assertions.assertEquals(0x52, register.read());

        Assertions.assertThrows(IllegalArgumentException.class, () -> register.set(0x100));
    }
}
