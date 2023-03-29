package com.github.dsvalerian.chip8.gui;

import javax.swing.*;

/**
 * This is the main window of the GUI.
 */
public class MainWindow extends JFrame {
    private static final int DEFAULT_HEIGHT = 540;
    private static final int DEFAULT_WIDTH = 960;
    private static final int GAME_HEIGHT = 360;
    private static final int GAME_WIDTH = 640;

    /**
     * The panel where the screen will be displayed.
     */
    private final JPanel screenPanel;

    /**
     * Construct the default {@link MainWindow}.
     */
    public MainWindow() {
        // Set up the window.
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setVisible(true);

        // Setting up the game panel.
        screenPanel = new JPanel();
        screenPanel.setSize(GAME_WIDTH, GAME_HEIGHT);
    }
}
