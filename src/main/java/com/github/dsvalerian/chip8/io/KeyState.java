package com.github.dsvalerian.chip8.io;

import java.util.function.IntConsumer;

/**
 * Singleton representation of the Chip-8 16-key keypad.
 * It's a singleton because there is only one keyboard state that we should
 * be tracking for the entire application.
 */
public class KeyState {
    private static KeyState instance = null;

    private final int NUM_KEYS = 16;
    private boolean[] keys = new boolean[NUM_KEYS];
    private IntConsumer onNextKeyPress = null;

    private int lastKeyPressed = -1;

    private KeyState() {
        // Overwriting it to make it private, but it doesn't actually do anything.
    }

    /**
     * @return A singleton instance of {@link KeyState}.
     */
    public static KeyState getInstance() {
        if (instance == null) {
            instance = new KeyState();
        }

        return instance;
    }

    /**
     * Press a key.
     * @param k The key to press, from 0-15 (inclusive).
     */
    public void press(int k) {
        if (k >= NUM_KEYS || k < 0) {
            throw new IllegalArgumentException("Cannot press a key outside of the key space " + NUM_KEYS);
        }

        keys[k] = true;
        lastKeyPressed = k;

        if (onNextKeyPress != null) {
            onNextKeyPress.accept(k);
            onNextKeyPress = null;
        }
    }

    /**
     * Release a key.
     * @param k The key to release, from 0-15 (inclusive).
     */
    public void release(int k) {
        if (k >= NUM_KEYS || k < 0) {
            throw new IllegalArgumentException("Cannot release a key outside of the key space " + NUM_KEYS);
        }

        keys[k] = false;
    }

    /**
     * Check if a key is pressed.
     * @param k The key to check.
     * @return True if key k is pressed.
     */
    public boolean isPressed(int k) {
        if (k >= NUM_KEYS || k < 0) {
            throw new IllegalArgumentException("Cannot release a key outside of the key space " + NUM_KEYS);
        }

        return keys[k];
    }

    /**
     * @return The value of the last key that was pressed.
     */
    public int getLastKeyPressed() {
        return lastKeyPressed;
    }

    /**
     * Set a function to be run after the next single key is pressed.
     * @param consumer The consumer to run once the next key is pressed. Requires a single int parameter.
     */
    public void setOnNextKeyPress(IntConsumer consumer) {
        this.onNextKeyPress = consumer;
    }
}
