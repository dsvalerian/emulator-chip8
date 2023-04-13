package com.github.dsvalerian.chip8.io;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.Register;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KeyStateTest {
    private static final KeyState KEY_STATE = KeyState.getInstance();

    @Test
    public void pressTest() {
        KEY_STATE.press(5);
        Assertions.assertTrue(KEY_STATE.isPressed(5));
        Assertions.assertEquals(5, KEY_STATE.getLastKeyPressed());
    }

    @Test
    public void releaseTest() {
        KEY_STATE.press(6);
        Assertions.assertTrue(KEY_STATE.isPressed(6));
        KEY_STATE.release(6);
        Assertions.assertFalse(KEY_STATE.isPressed(6));
        Assertions.assertEquals(6, KEY_STATE.getLastKeyPressed());
    }

    @Test
    public void invalidKeysTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> KEY_STATE.press(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KEY_STATE.press(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KEY_STATE.release(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KEY_STATE.release(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KEY_STATE.isPressed(16));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KEY_STATE.isPressed(-1));
    }

    @Test
    public void onNextKeyPressTest() {
        Register lastPressedKey = new Register(Bits.EIGHT);

        KEY_STATE.setOnNextKeyPress((pressedKey) -> {
            lastPressedKey.set(pressedKey);
        });

        KEY_STATE.press(15);
        KEY_STATE.press(3);

        Assertions.assertEquals(15, lastPressedKey.read());
    }
}
