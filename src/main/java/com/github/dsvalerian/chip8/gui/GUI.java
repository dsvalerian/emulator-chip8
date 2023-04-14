package com.github.dsvalerian.chip8.gui;

import com.github.dsvalerian.chip8.io.KeyState;
import com.github.dsvalerian.chip8.io.ScreenState;

import javax.swing.*;

/**
 * This is the main window of the GUI.
 */
public class GUI extends JFrame {
    private static final String WINDOW_TITLE = "Chip-8 Emulator";

    /**
     * The menu bar used for this window.
     */
    private final MainMenuBar menuBar = new MainMenuBar();

    /**
     * The panel where the screen pixels are drawn to.
     */
    private final ScreenPanel screenPanel = new ScreenPanel();

    private static GUI instance = null;

    /**
     * Construct the default {@link GUI}.
     */
    private GUI() {
        // Set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle(WINDOW_TITLE);

        // Setting up the menu bar.
        setJMenuBar(menuBar);

        // Setting up the game panel.
        add(screenPanel);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * @return A singleton instance of {@link GUI}, the main window of the UI.
     */
    public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }

        return instance;
    }

    /**
     * Draw the screen to the screen panel.
     * @param screen The screen data to draw.
     */
    public void drawScreen(ScreenState screen) {
        screenPanel.drawScreen(screen);
        screenPanel.repaint();
    }

    /**
     * Update the title of the window to include the filename.
     * @param fileName The name of the program currently running.
     */
    public static void updateTitleWithFileName(String fileName) {
        getInstance().setTitle(WINDOW_TITLE + " [" + fileName + "]");
    }
}
