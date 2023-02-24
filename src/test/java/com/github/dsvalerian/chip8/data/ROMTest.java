package com.github.dsvalerian.chip8.data;

import com.github.dsvalerian.chip8.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ROMTest {
    @Test
    public void fromBytesTest() {
        int[] bytes = {0x12, 0x55, 0xFF, 0x00, 0x9B};
        ROM rom = ROM.fromBytes(bytes);

        for (int i = 0; i < rom.getSize(); i++) {
            Assertions.assertEquals(bytes[i], rom.read(i));
        }
    }

    @Test
    public void fromHexStringTest() {
        int[] bytes = {0x12, 0x55, 0xFF, 0x00, 0x9B};
        String hexString = "12 55 FF 00 9B";
        ROM rom = ROM.fromHexString(hexString);

        for (int i = 0; i < rom.getSize(); i++) {
            Assertions.assertEquals(bytes[i], rom.read(i));
        }

        String invalidHexString = "12 5D 98F 00";
        Assertions.assertThrows(IllegalArgumentException.class, () -> ROM.fromHexString(invalidHexString));
    }

    @Test
    public void fromFileTest() {
        int[] bytes = {0x12, 0x55, 0xFF, 0x00, 0x9B};
        URL romTestResource = ROMTest.class.getClassLoader().getResource("romtest.ch8");

        if (romTestResource == null) {
            Assertions.fail("could not get resource 'romtest.ch8'");
        }

        ROM rom;

        try {
            rom = ROM.fromFile(Paths.get(romTestResource.toURI()).toString());

            for (int i = 0; i < rom.getSize(); i++) {
                Assertions.assertEquals(bytes[i], rom.read(i));
            }

            rom = ROM.fromFile(Paths.get(romTestResource.toURI()));

            for (int i = 0; i < rom.getSize(); i++) {
                Assertions.assertEquals(bytes[i], rom.read(i));
            }
        }
        catch (URISyntaxException ex) {
            ex.printStackTrace();
            Assertions.fail("URISyntaxException when getting resource file path");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            Assertions.fail("IOException when reading file");
        }
    }

    @Test
    public void setterTest() {
        int someInt = 0x00;
        Register someRegister = new Register(Constants.EIGHT_BIT_SIZE);

        ROM rom = ROM.fromBytes(new int[0]);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> rom.set(someInt, someRegister));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> rom.set(someRegister, someInt));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> rom.set(someRegister, someRegister));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> rom.set(someInt, someInt));
    }
}
