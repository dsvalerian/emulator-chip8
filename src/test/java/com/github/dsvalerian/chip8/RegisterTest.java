package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTest {
    private Register register;

    @Test
    public void constructorTest() {
        register = new Register(Constants.EIGHT_BIT_SIZE);
        Assertions.assertEquals(0, register.read());
    }

    @Test
    public void invalidConstructorTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> register = new Register(33));
    }

    @Test
    public void setterGetterTest() {
        register = new Register(Constants.EIGHT_BIT_SIZE);
        register.set(0xFF);
        Assertions.assertEquals(0xFF, register.read());

        Assertions.assertThrows(IllegalArgumentException.class, () -> register.set(0x100));
    }
}
