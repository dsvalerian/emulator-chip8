package com.github.dsvalerian.chip8.gui;

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
     * Create a new {@link MainMenuBar}.
     */
    public MainMenuBar() {
        // File menu.
        loadROMItem.addActionListener(new LoadROMListener());
        exitItem.addActionListener(new ExitItemListener());
        fileMenu.add(loadROMItem);
        fileMenu.add(exitItem);

        // Adding menus to bar.
        add(fileMenu);
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
}
