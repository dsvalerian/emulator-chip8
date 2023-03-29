package com.github.dsvalerian.chip8.io;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.Register;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeyboardTest {
    private Keyboard keyboard;

    @BeforeEach
    public void setUp() {
        keyboard = new Keyboard();
    }

    @Test
    public void pressTest() {
        keyboard.press(5);
        Assertions.assertTrue(keyboard.isPressed(5));
        Assertions.assertEquals(5, keyboard.getLastKeyPressed());
    }

    @Test
    public void releaseTest() {
        keyboard.press(6);
        Assertions.assertTrue(keyboard.isPressed(6));
        keyboard.release(6);
        Assertions.assertFalse(keyboard.isPressed(6));
        Assertions.assertEquals(6, keyboard.getLastKeyPressed());
    }

    @Test
    public void invalidKeysTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyboard.press(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyboard.press(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyboard.release(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyboard.release(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyboard.isPressed(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyboard.isPressed(-1));
    }

    @Test
    public void onNextKeyPressTest() {
        Register lastPressedKey = new Register(Bits.EIGHT);

        keyboard.setOnNextKeyPress((pressedKey) -> {
            lastPressedKey.set(pressedKey);
        });

        keyboard.press(15);
        keyboard.press(3);

        Assertions.assertEquals(15, lastPressedKey.read());
    }
}
