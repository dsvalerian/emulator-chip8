package com.github.dsvalerian.chip8.data;

import com.github.dsvalerian.chip8.util.Constants;

/**
 * Representation of a block of read-only memory containing byte data, where each register
 * in the block is one byte.
 */
public class ROM extends MemoryBlock {
    private ROM(int[] data, int registerBits) {
        super(data, registerBits);
    }

    /**
     * Return a new ROM block containing byte data.
     *
     * @param bytes The byte data.
     * @return A new {@link ROM}.
     */
    public static ROM fromBytes(int[] bytes) {
        return new ROM(bytes, Constants.EIGHT_BIT_SIZE);
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
            bytes[i] = Integer.parseInt(hexBytes[i], 16);
        }

        return new ROM(bytes, Constants.EIGHT_BIT_SIZE);
    }

    /**
     * Return a new ROM containing byte data from a file.
     *
     * @param filePath The path to the file.
     * @return A new {@link ROM}.
     */
    public static ROM fromFile(String filePath) {
        // todo
        return null;
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
