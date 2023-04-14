package com.github.dsvalerian.chip8.gui;

/**
 * Frames per second enum.
 */
public enum FPS {
    SIXTY(60);

    private int fps;

    FPS(int fps) {
        this.fps = fps;
    }

    /**
     * @return The number of frames per second for this enum value.
     */
    public int getValue() {
        return fps;
    }
}
