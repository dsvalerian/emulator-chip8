package com.github.dsvalerian.chip8;

/**
 * A block of memory representing an array of {@link Byte}s.
 */
public class MemoryBlock {
    private Register[] memory;

    /**
     * Construct a {@link MemoryBlock} of a certain size, with each register capable of storing
     * a specified number of bits.
     *
     * @param memorySize Size of memory block.
     * @param registerBits Number of bits each {@link Register} in the block can store.
     */
    public MemoryBlock(int memorySize, int registerBits) {
        memory = new Register[memorySize];
        for (int i = 0; i < memorySize; i++) {
            memory[i] = new Register(registerBits);
        }
    }

    /**
     * Get the value at a specified address.
     *
     * @param address A {@link Register} that is storing the address to get.
     * @return The value at the address stored in the {@link Register}.
     */
    public int get(Register address) {
        return get(address.read());
    }

    /**
     * Get the value at a specified address.
     *
     * @param address The address to get the byte from.
     * @return The value at the specified address.
     */
    public int get(int address) {
        if (address >= this.getSize() || address < 0x00) {
            throw new IllegalArgumentException("address must be within memory space of " + this.getSize() + " bytes");
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
            throw new IllegalArgumentException("address must be within memory space of " +
                    this.getSize() + " registers");
        }

        memory[address].set(value);
    }

    public int getSize() {
        return memory.length;
    }
}