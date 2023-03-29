package com.github.dsvalerian.chip8.gui;

import javax.swing.*;

public class MainWindow extends JFrame {
    private static final int DEFAULT_HEIGHT = 540;
    private static final int DEFAULT_WIDTH = 960;
    private static final int GAME_HEIGHT = 360;
    private static final int GAME_WIDTH = 640;

    private JPanel gamePanel;

    /**
     * Construct the main window of the GUI.
     */
    public MainWindow() {
        // Set up the window.
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setVisible(true);

        // Setting up the game panel.
        gamePanel = new JPanel();
        gamePanel.setSize(GAME_WIDTH, GAME_HEIGHT);
    }
}
