package com.github.dsvalerian.chip8.data;

/**
 * A block of memory representing an array of {@link Byte}s.
 */
public class MemoryBlock {
    private Register[] memory;

    /**
     * Construct a {@link MemoryBlock} of a certain size, with each register capable of storing
     * a specified number of bits.
     *
     * @param size Size of the memory block.
     * @param registerSize Size of each {@link Register} in the block.
     */
    public MemoryBlock(int size, Bits registerSize) {
        memory = new Register[size];
        for (int i = 0; i < size; i++) {
            memory[i] = new Register(registerSize);
        }
    }

    /**
     * Construct a {@link MemoryBlock} from a data array, with each register capable of storing
     * a specified number of bits.
     *
     * @param data The data array. Each element must be able to fit in a register.
     * @param registerSize Size of each {@link Register} in the block.
     */
    public MemoryBlock(int[] data, Bits registerSize) {
        memory = new Register[data.length];
        for (int i = 0; i < data.length; i++) {
            memory[i] = new Register(registerSize);
            memory[i].set(data[i]);
        }
    }

    /**
     * Read the value at a specified address.
     *
     * @param address A {@link Register} that is storing the address to get.
     * @return The value at the address stored in the {@link Register}.
     */
    public int read(Register address) {
        return read(address.read());
    }

    /**
     * Read the value at a specified address.
     *
     * @param address The address to get the byte from.
     * @return The value at the specified address.
     */
    public int read(int address) {
        if (address >= this.getSize() || address < 0x00) {
            throw new IllegalArgumentException("Address must be within memory space of " + this.getSize() + " registers.");
        }

        return memory[address].read();
    }

    /**
     * Set a value at a specified address.
     *
     * @param address The address to set.
     * @param value A {@link Register} that is storing the value to set.
     */
    public void set(int address, Register value) {
        set(address, value.read());
    }

    /**
     * Set a value at a specified address.
     *
     * @param address A {@link Register} that is storing the address to set.
     * @param value The value to set.
     */
    public void set(Register address, int value) {
        set(address.read(), value);
    }

    /**
     * Set a value at a specified address.
     *
     * @param address A {@link Register} that is storing the address to set.
     * @param value A {@link Register} that is storing the value to set.
     */
    public void set(Register address, Register value) {
        set(address.read(), value.read());
    }

    /**
     * Set a value at a specified address.
     *
     * @param address The address to set.
     * @param value The value to set.
     */
    public void set(int address, int value) {
        if (address >= this.getSize() || address < 0x00) {
            throw new IllegalArgumentException("Address must be within memory space of " +
                    this.getSize() + " registers.");
        }

        memory[address].set(value);
    }

    /**
     * Get the size of the memory block.
     *
     * @return The number of registers in the block.
     */
    public int getSize() {
        return memory.length;
    }
}
