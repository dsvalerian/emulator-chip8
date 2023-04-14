package com.github.dsvalerian.chip8.gui;

import com.github.dsvalerian.chip8.io.Pixel;
import com.github.dsvalerian.chip8.io.ScreenState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The panel to which screen data will be drawn. AKA, if you're emulating a game, it will be drawn here.
 */
public class ScreenPanel extends JPanel {
    private static final int ASPECT_RATIO = ScreenState.WIDTH / ScreenState.HEIGHT;
    private static final int GAME_HEIGHT = 640;
    private static final int GAME_WIDTH = GAME_HEIGHT * ASPECT_RATIO;
    private static final int PIXEL_SIZE = GAME_WIDTH / ScreenState.WIDTH;
    private static final Color ACTIVE_PIXEL_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = Color.BLACK;

    /**
     * The {@link BufferedImage} to which pixels are drawn.
     */
    private BufferedImage canvas = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);

    /**
     * Create a new {@link ScreenPanel}.
     */
    public ScreenPanel() {
        setDoubleBuffered(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.drawImage(canvas, null, null);
    }

    private void drawRect(int xCoord, int yCoord, int width, int height, Color color) {
        int xEnd = xCoord + width;
        int yEnd = yCoord + height;

        for (int x = xCoord; x < xEnd; x++) {
            for (int y = yCoord; y < yEnd; y++) {
                canvas.setRGB(x, y, color.getRGB());
            }
        }
    }

    /**
     * Draw screen to the screen canvas.
     * @param screen The {@link ScreenState} containing the screen data.
     */
    public void drawScreen(ScreenState screen) {
        for (int x = 0; x < ScreenState.WIDTH; x++) {
            for (int y = 0; y < ScreenState.HEIGHT; y++) {
                int xCoord = x * PIXEL_SIZE;
                int yCoord = y * PIXEL_SIZE;

                if (screen.readPixel(x, y) == Pixel.ACTIVE) {
                    drawRect(xCoord, yCoord, PIXEL_SIZE, PIXEL_SIZE, ACTIVE_PIXEL_COLOR);
                }
                else {
                    drawRect(xCoord, yCoord, PIXEL_SIZE, PIXEL_SIZE, BACKGROUND_COLOR);
                }
            }
        }
    }
}
