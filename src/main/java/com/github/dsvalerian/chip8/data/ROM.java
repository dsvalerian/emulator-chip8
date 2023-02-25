package com.github.dsvalerian.chip8.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Representation of a block of read-only memory containing byte data, where each register
 * in the block is one byte.
 */
public class ROM extends MemoryBlock {
    private ROM(int[] data, Bits registerBits) {
        super(data, registerBits);
    }

    /**
     * Return a new ROM block containing byte data.
     *
     * @param bytes The byte data.
     * @return A new {@link ROM}.
     */
    public static ROM fromBytes(int[] bytes) {
        return new ROM(bytes, Bits.EIGHT);
    }

    /**
     * Return a new ROM block containing byte data parsed from a hex string in the
     * following format: "byte byte byte".
     * Example: "05 FF D3 F1 49".
     *
     * @param hexString The string containing whitespace-separated bytes in hex form.
     * @return A new {@link ROM}.
     */
    public static ROM fromHexString(String hexString) {
        String[] hexBytes = hexString.split(" ");
        int[] bytes = new int[hexBytes.length];

        // Parse string elements into bytes (assuming hex form).
        for (int i = 0; i < bytes.length; i++) {
            if (hexBytes[i].length() != 2) {
                throw new IllegalArgumentException("provided hex string is invalid");
            }

            bytes[i] = Integer.parseInt(hexBytes[i], 16);
        }

        return new ROM(bytes, Bits.EIGHT);
    }

    /**
     * Return a new ROM containing byte data from a file.
     *
     * @param filePath The {@link Path} to the file.
     * @return A new {@link ROM} or null if filepath is incorrect.
     * @throws IOException if given file path is not valid or there is issue reading the bytes.
     */
    public static ROM fromFile(Path filePath) throws IOException {
        return fromFile(filePath.toString());
    }

    /**
     * Return a new ROM containing byte data from a file.
     *
     * @param filePath The path to the file.
     * @return A new {@link ROM} or null if filepath is incorrect.
     * @throws IOException if given file path is not valid or there is issue reading the bytes.
     */
    public static ROM fromFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.length() >= Integer.MAX_VALUE) {
            throw new UnsupportedOperationException("cannot read a file greater than " + Integer.MAX_VALUE + " bytes");
        }

        // Get bytes array from file.
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);

        // Convert bytes array to int array.
        int[] newBytes = new int[bytes.length];
        for (int i = 0; i < newBytes.length; i++) {
            newBytes[i] = Byte.toUnsignedInt(bytes[i]);
        }

        return new ROM(newBytes, Bits.EIGHT);
    }

    /**
     * Return a new empty ROM.
     *
     * @return An empty {@link ROM}.
     */
    public static ROM fromEmpty() {
        return new ROM(new int[0], Bits.EIGHT);
    }

    /**
     * Unsupported operation. Cannot set an address in read-only memory.
     */
    @Override
    public void set(int address, Register value) {
        throw new UnsupportedOperationException("cannot set an address in a block of read-only memory");
    }

    /**
     * Unsupported operation. Cannot set an address in read-only memory.
     */
    @Override
    public void set(Register address, int value) {
        throw new UnsupportedOperationException("cannot set an address in a block of read-only memory");
    }

    /**
     * Unsupported operation. Cannot set an address in read-only memory.
     */
    @Override
    public void set(Register address, Register value) {
        throw new UnsupportedOperationException("cannot set an address in a block of read-only memory");
    }

    /**
     * Unsupported operation. Cannot set an address in read-only memory.
     */
    @Override
    public void set(int address, int value) {
        throw new UnsupportedOperationException("cannot set an address in a block of read-only memory");
    }
}
