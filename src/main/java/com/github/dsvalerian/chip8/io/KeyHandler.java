package com.github.dsvalerian.chip8.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * An extension of {@link KeyListener} that listens to {@link KeyEvent}s
 * and updates the singleton {@link KeyState} accordingly.
 */
public class KeyHandler implements KeyListener {
    private static final Map<Integer, Integer> KEY_MAP;
    private static final KeyState STATE = KeyState.getInstance();

    private static KeyHandler instance = null;

    static {
        KEY_MAP = new HashMap<>();
        KEY_MAP.put(KeyEvent.VK_1, 1);
        KEY_MAP.put(KeyEvent.VK_2, 2);
        KEY_MAP.put(KeyEvent.VK_3, 3);
        KEY_MAP.put(KeyEvent.VK_4, 0xC);
        KEY_MAP.put(KeyEvent.VK_Q, 4);
        KEY_MAP.put(KeyEvent.VK_W, 5);
        KEY_MAP.put(KeyEvent.VK_E, 6);
        KEY_MAP.put(KeyEvent.VK_R, 0xD);
        KEY_MAP.put(KeyEvent.VK_A, 7);
        KEY_MAP.put(KeyEvent.VK_S, 8);
        KEY_MAP.put(KeyEvent.VK_D, 9);
        KEY_MAP.put(KeyEvent.VK_F, 0xE);
        KEY_MAP.put(KeyEvent.VK_Z, 0xA);
        KEY_MAP.put(KeyEvent.VK_X, 0);
        KEY_MAP.put(KeyEvent.VK_C, 0xB);
        KEY_MAP.put(KeyEvent.VK_V, 0xF);
    }

    private KeyHandler() {
        // Overwritten so it's private, but doesn't actually do anything special.
    }

    /**
     * @return A singleton instance of {@link KeyHandler}.
     */
    public static KeyHandler getInstance() {
        if (instance == null) {
            instance = new KeyHandler();
        }

        return instance;
    }

    /**
     * This is unhandled, does nothing.
     * @param event The event to be processed.
     */
    @Override
    public void keyTyped(KeyEvent event) {

    }

    /**
     * Called when a key is pressed. Sets the corresponding key to be
     * pressed in the {@link KeyState} singleton.
     * @param event The event to be processed.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        Integer pressedKey = KEY_MAP.get(event.getKeyCode());

        if (pressedKey != null) {
            STATE.press(pressedKey);
        }
    }

    /**
     * Called when a key is released. Sets the corresponding key to be
     * released in the {@link KeyState} singleton.
     * @param event The event to be processed.
     */
    @Override
    public void keyReleased(KeyEvent event) {
        Integer pressedKey = KEY_MAP.get(event.getKeyCode());

        if (pressedKey != null) {
            STATE.release(pressedKey);
        }
    }

    @Override
    public String toString() {
        return STATE.toString();
    }
}
