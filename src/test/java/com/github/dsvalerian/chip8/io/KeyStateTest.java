package com.github.dsvalerian.chip8.io;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.Register;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeyStateTest {
    private KeyState keyState;

    @BeforeEach
    public void setUp() {
        keyState = new KeyState();
    }

    @Test
    public void pressTest() {
        keyState.press(5);
        Assertions.assertTrue(keyState.isPressed(5));
        Assertions.assertEquals(5, keyState.getLastKeyPressed());
    }

    @Test
    public void releaseTest() {
        keyState.press(6);
        Assertions.assertTrue(keyState.isPressed(6));
        keyState.release(6);
        Assertions.assertFalse(keyState.isPressed(6));
        Assertions.assertEquals(6, keyState.getLastKeyPressed());
    }

    @Test
    public void invalidKeysTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyState.press(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyState.press(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyState.release(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyState.release(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyState.isPressed(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> keyState.isPressed(-1));
    }

    @Test
    public void onNextKeyPressTest() {
        Register lastPressedKey = new Register(Bits.EIGHT);

        keyState.setOnNextKeyPress((pressedKey) -> {
            lastPressedKey.set(pressedKey);
        });

        keyState.press(15);
        keyState.press(3);

        Assertions.assertEquals(15, lastPressedKey.read());
    }
}
