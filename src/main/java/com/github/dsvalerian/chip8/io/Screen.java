package com.github.dsvalerian.chip8.io;

/**
 * Represents a Chip-8 screen. Handles setting and reading pixels.
 */
public class Screen {
    /**
     * The width of the screen.
     */
    public static final int WIDTH = 64;
    /**
     * The height of the screen.
     */
    public static final int HEIGHT = 32;

    private boolean[][] pixels = new boolean[WIDTH][HEIGHT];

    /**
     * Set a pixel at (x, y) to either active or inactive.
     *
     * @param x The column in the screen.
     * @param y The row in the screen.
     * @param active 1 or 0 for the pixel at (x, y) to be active or inactive.
     */
    public void setPixel(int x, int y, boolean active) {
        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
            throw new IllegalArgumentException("Cannot set a pixel outside of the screen space " +
                    WIDTH + " * " + HEIGHT + ".");
        }

        pixels[x][y] = active;
    }

    /**
     * @param x The column in the screen.
     * @param y The row in the screen.
     * @return 1 if the pixel at (x, y) is active, 0 otherwise.
     */
    public boolean readPixel(int x, int y) {
        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
            throw new IllegalArgumentException("Cannot get a pixel outside of the screen space " +
                    WIDTH + " * " + HEIGHT);
        }

        return pixels[x][y];
    }

    /**
     * Deactivate every pixel in the screen.
     */
    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                setPixel(i, j, false);
            }
        }
    }
}
