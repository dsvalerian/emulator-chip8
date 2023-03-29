package com.github.dsvalerian.chip8.data;

import com.github.dsvalerian.chip8.CPUState;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains some sprite-specific functions for loading sprite data and looking up addresses that point
 * to sprites.
 */
public class Sprites {
    private static final int SPRITES_START_ADDRESS = 0x0;

    // See last image here: https://laurencescotford.com/chip-8-on-the-cosmac-vip-the-character-set/
    private static final String SPRITES_DATA = "F0 90 90 90 F0 20 60 20 20 70 F0 10 F0 80 F0 F0 10 F0 " +
            "10 F0 90 90 F0 10 10 F0 80 F0 10 F0 F0 80 F0 90 F0 F0 10 20 40 40 F0 90 F0 90 F0 F0 90 " +
            "F0 10 F0 F0 90 F0 80 80 E0 90 E0 90 E0 F0 80 80 80 F0 E0 90 90 90 E0 F0 80 F0 80 F0 F0 " +
            "80 F0 80 80";

    private static final Map<Integer, Integer> SPRITES_ADDRESS_TABLE = new HashMap<>() {{
        put(0x0, SPRITES_START_ADDRESS + 0);
        put(0x1, SPRITES_START_ADDRESS + 5);
        put(0x2, SPRITES_START_ADDRESS + 10);
        put(0x3, SPRITES_START_ADDRESS + 15);
        put(0x4, SPRITES_START_ADDRESS + 20);
        put(0x5, SPRITES_START_ADDRESS + 25);
        put(0x6, SPRITES_START_ADDRESS + 30);
        put(0x7, SPRITES_START_ADDRESS + 35);
        put(0x8, SPRITES_START_ADDRESS + 40);
        put(0x9, SPRITES_START_ADDRESS + 45);
        put(0xA, SPRITES_START_ADDRESS + 50);
        put(0xB, SPRITES_START_ADDRESS + 55);
        put(0xC, SPRITES_START_ADDRESS + 60);
        put(0xD, SPRITES_START_ADDRESS + 65);
        put(0xE, SPRITES_START_ADDRESS + 70);
        put(0xF, SPRITES_START_ADDRESS + 75);
    }};

    /**
     * Load the sprite data into main memory of the provided {@link CPUState} at address 0x0.
     *
     * @param state The {@link CPUState} into which the sprites will be loaded.
     */
    public static void load(CPUState state) {
        state.loadMemory(SPRITES_START_ADDRESS, ROM.fromHexString(SPRITES_DATA));
    }

    /**
     * Get the address in main memory associated with the provided value.
     *
     * @param value The hex value, from 0x0-0xF, for which to get the address of the associated sprite.
     * @return The address associated with the provided hex value.
     */
    public static int lookUp(int value) {
        return SPRITES_ADDRESS_TABLE.get(value);
    }
}
