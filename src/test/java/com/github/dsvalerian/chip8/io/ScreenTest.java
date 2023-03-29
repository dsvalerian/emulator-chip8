package com.github.dsvalerian.chip8.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScreenTest {
    private Screen screen;

    @BeforeEach
    public void setUp() {
        screen = new Screen();
    }

    @Test
    public void setPixelTest() {
        screen.setPixel(5, 10, true);
        Assertions.assertTrue(screen.readPixel(5, 10));
        screen.setPixel(5, 10, false);
        Assertions.assertFalse(screen.readPixel(5, 10));
    }

    @Test
    public void clearScreenTest() {
        for (int i = 0; i < 32; i++) {
            screen.setPixel(i, i, true);
            Assertions.assertTrue(screen.readPixel(i, i));
        }

        screen.clear();

        for (int i = 0; i < 32; i++) {
            Assertions.assertFalse(screen.readPixel(i, i));
        }
    }

    @Test
    public void invalidPixelsTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> screen.setPixel(31, 32, true));
        Assertions.assertThrows(IllegalArgumentException.class, () -> screen.setPixel(64, 31, true));
        Assertions.assertThrows(IllegalArgumentException.class, () -> screen.readPixel(31, 32));
        Assertions.assertThrows(IllegalArgumentException.class, () -> screen.readPixel(64, 31));
    }

    @Test
    public void toStringTest() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < screen.HEIGHT; i++) {
            for (int j = 0; j < screen.WIDTH; j++) {
                builder.append(". ");
            }

            builder.append("\n");
        }

        Assertions.assertEquals(builder.toString(), screen.toString());
    }
}
