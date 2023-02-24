package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.Register;

/**
 * Representation of the CHIP-8 CPU state. Keeps track of all memory, registers, counters, delays, etc, and include
 * read and set methods for each.
 */
public class CPUState {
    private MemoryBlock memory;
    private MemoryBlock stack;

    private MemoryBlock vRegisters;

    private Register iRegister;
    private Register pcRegister;
    private Register spRegister;

    public CPUState(int memorySize, Bits memoryRegisterBits,
                    int stackSize, Bits stackRegisterBits, Bits spRegisterBits,
                    int numVRegisters, Bits vRegisterBits,
                    Bits iRegisterBits, Bits pcRegisterBits) {
        memory = new MemoryBlock(memorySize, memoryRegisterBits);
        stack = new MemoryBlock(stackSize, stackRegisterBits);

        vRegisters = new MemoryBlock(numVRegisters, vRegisterBits);

        iRegister = new Register(iRegisterBits);
        pcRegister = new Register(pcRegisterBits);
        spRegister = new Register(spRegisterBits);
    }

    /**
     * Read the value at an address in main memory.
     *
     * @param address The address to read.
     * @return The value at the address.
     */
    public int readMemory(int address) {
        return memory.read(address);
    }

    /**
     * Set a value at an address in main memory.
     *
     * @param address The address at which to set the value.
     * @param value The value to set.
     */
    public void setMemory(int address, int value) {
        memory.set(address, value);
    }

    /**
     * Pop the top value off the stack and set the SP register back.
     *
     * @return The popped value.
     */
    public int popStack() {
        int value = stack.read(spRegister.read());
        spRegister.set(spRegister.read() - 1);
        return value;
    }

    /**
     * Push a value to the top of the stack and move the SP register up.
     *
     * @param value The value to push to the stack.
     */
    public void pushStack(int value) {
        spRegister.set(spRegister.read() + 1);
        stack.set(spRegister, value);
    }

    /**
     * Read the value stored in the Vx register, where x is the provided register number 0-15 inclusive.
     *
     * @param x The register number.
     * @return The value stored in the Vx register.
     */
    public int readV(int x) {
        return vRegisters.read(x);
    }

    /**
     * Set the value in the Vx register, where x is the provided register number 0-15 inclusive.
     *
     * @param x The register number.
     * @param value The value to set.
     */
    public void setV(int x, int value) {
        vRegisters.set(x, value);
    }

    /**
     * Read the value stored in the I register.
     *
     * @return The value.
     */
    public int readI() {
        return iRegister.read();
    }

    /**
     * Set the value stored in the I register.
     *
     * @param value The value.
     */
    public void setI(int value) {
        iRegister.set(value);
    }

    /**
     * Set the value stored in the PC register.
     *
     * @return The value.
     */
    public int readPc() {
        return pcRegister.read();
    }

    /**
     * Set the value stored in the PC register.
     *
     * @param value The value.
     */
    public void setPc(int value) {
        pcRegister.set(value);
    }
}
