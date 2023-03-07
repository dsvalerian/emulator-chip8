package com.github.dsvalerian.chip8;

/**
 * Representation of the CPU state. Keeps track of all memory, registers, counters, delays, etc, and include
 * read and set methods for each.
 */
public interface CPUState {
    /**
     * Read the value at an address in main memory.
     *
     * @param address The address to read.
     * @return The value at the address.
     */
    int readMemory(int address);

    /**
     * Set a value at an address in main memory.
     *
     * @param address The address at which to set the value.
     * @param value The value to set.
     */
    void setMemory(int address, int value);

    /**
     * Set the SP register back and pop the value off the top.
     *
     * @return The popped value.
     */
    int popStack();

    /**
     * Push a value to the top of the stack and move the SP register up.
     *
     * @param value The value to push to the stack.
     */
    void pushStack(int value);

    /**
     * Read the value stored in the Vx register, where x is the provided register number 0-15 inclusive.
     *
     * @param x The register number.
     * @return The value stored in the Vx register.
     */
    int readV(int x);

    /**
     * Set the value in the Vx register, where x is the provided register number 0-15 inclusive.
     *
     * @param x The register number.
     * @param value The value to set.
     */
    void setV(int x, int value);

    /**
     * Read the value stored in the I register.
     *
     * @return The value.
     */
    int readI();

    /**
     * Set the value stored in the I register.
     *
     * @param value The value.
     */
    void setI(int value);

    /**
     * Read the value stored in the PC register.
     *
     * @return The value.
     */
    int readPc();

    /**
     * Set the value stored in the PC register.
     *
     * @param value The value.
     */
    void setPc(int value);

    /**
     * Read the value stored in the delay timer register.
     *
     * @return The value.
     */
    int readDt();

    /**
     * Set the value stored in the delay timer register.
     *
     * @param value The value.
     */
    void setDt(int value);

    /**
     * Read the value stored in the sound timer register.
     *
     * @return The value.
     */
    int readSt();

    /**
     * Set the value stored in the sound timer register.
     *
     * @param value The value.
     */
    void setSt(int value);
}
