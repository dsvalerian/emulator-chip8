package com.github.dsvalerian.chip8.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScreenStateTest {
    private ScreenState screenState;

    @BeforeEach
    public void setUp() {
        screenState = new ScreenState();
    }

    @Test
    public void setPixelTest() {
        screenState.setPixel(5, 10, Pixel.ACTIVE);
        Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(5, 10));
        screenState.setPixel(5, 10, Pixel.INACTIVE);
        Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(5, 10));
    }

    @Test
    public void clearScreenTest() {
        for (int i = 0; i < 32; i++) {
            screenState.setPixel(i, i, Pixel.ACTIVE);
            Assertions.assertEquals(Pixel.ACTIVE, screenState.readPixel(i, i));
        }

        screenState.clear();

        for (int i = 0; i < 32; i++) {
            Assertions.assertEquals(Pixel.INACTIVE, screenState.readPixel(i, i));
        }
    }

    @Test
    public void invalidPixelsTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> screenState.setPixel(31, 32, Pixel.ACTIVE));
        Assertions.assertThrows(IllegalArgumentException.class, () -> screenState.setPixel(64, 31, Pixel.ACTIVE));
        Assertions.assertThrows(IllegalArgumentException.class, () -> screenState.readPixel(31, 32));
        Assertions.assertThrows(IllegalArgumentException.class, () -> screenState.readPixel(64, 31));
    }

    @Test
    public void toStringTest() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < screenState.HEIGHT; i++) {
            for (int j = 0; j < screenState.WIDTH; j++) {
                builder.append(". ");
            }

            builder.append("\n");
        }

        Assertions.assertEquals(builder.toString(), screenState.toString());
    }
}
