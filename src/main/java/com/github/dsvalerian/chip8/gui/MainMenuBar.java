package com.github.dsvalerian.chip8.gui;

import com.github.dsvalerian.chip8.Emulator;
import com.github.dsvalerian.chip8.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The menu bar that shows up for the main GUI screen.
 */
public class MainMenuBar extends JMenuBar {
    /**
     * Name of the file menu.
     */
    private JMenu fileMenu = new JMenu("File");

    /**
     * Name of the Load Rom item in the file menu.
     */
    private JMenuItem loadROMItem = new JMenuItem("Load ROM...");

    /**
     * Name of the exit item in the file menu.
     */
    private JMenuItem exitItem = new JMenuItem("Exit");

    /**
     * Button for toggling the emulator to play/pause.
     */
    private JButton playPauseButton = new JButton("\u23EF");

    /**
     * Button for running the next emulator update. Only works when
     * the emulator is paused.
     */
    private JButton nextUpdateButton = new JButton("\u23E9");

    /**
     * Create a new {@link MainMenuBar}.
     */
    public MainMenuBar() {
        // File menu.
        loadROMItem.addActionListener(new LoadROMListener());
        exitItem.addActionListener(new ExitItemListener());
        fileMenu.add(loadROMItem);
        fileMenu.add(exitItem);

        playPauseButton.addActionListener(new PlayPauseButtonListener());
        playPauseButton.setFocusPainted(false);
        nextUpdateButton.addActionListener(new NextUpdateButtonListener());
        nextUpdateButton.setFocusPainted(false);

        // Adding menus to bar.
        add(fileMenu);
        add(playPauseButton);
        add(nextUpdateButton);
    }

    class LoadROMListener implements ActionListener {
        JFileChooser fileChooser = new JFileChooser();

        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fileChooser.showOpenDialog(GUI.getInstance());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Main.createAndRunEmulatorForProgram(fileChooser.getSelectedFile());
            }
        }
    }

    class ExitItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class PlayPauseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Emulator currentEmulator = Main.getCurrentEmulator();
            if (currentEmulator != null) {
                if (currentEmulator.isPaused()) {
                    currentEmulator.resume();
                }
                else {
                    currentEmulator.pause();
                }
            }
        }
    }

    class NextUpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Emulator currentEmulator = Main.getCurrentEmulator();
            if (currentEmulator != null) {
                if (currentEmulator.isPaused()) {
                    currentEmulator.update();
                }
            }
        }
    }
}
