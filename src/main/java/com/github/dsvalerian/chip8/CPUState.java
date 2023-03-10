package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.FullStackException;

import java.util.EmptyStackException;

/**
 * Representation of the Chip-8 CPU state. Keeps track of all memory, registers, counters, delays, etc, and include
 * read and set methods for each.
 */
public class CPUState {
    private final int MEMORY_SIZE = 4096;
    private final Bits MEMORY_REGISTER_SIZE = Bits.EIGHT;
    private final int STACK_SIZE = 16;
    private final Bits STACK_REGISTER_SIZE = Bits.SIXTEEN;
    private final Bits STACK_POINTER_SIZE = Bits.EIGHT;
    private final int NUM_V_REGISTERS = 16;
    private final Bits V_REGISTER_SIZE = Bits.EIGHT;
    private final Bits I_REGISTER_SIZE = Bits.TWELVE;
    private final Bits PROGRAM_COUNTER_SIZE = Bits.SIXTEEN;
    private final Bits DELAY_TIMER_SIZE = Bits.EIGHT;
    private final Bits SOUND_TIMER_SIZE = Bits.EIGHT;

    private MemoryBlock memory;
    private MemoryBlock stack;
    private Register stackPointer;
    private MemoryBlock vRegisters;
    private Register iRegister;
    private Register programCounter;
    private Register delayTimer;
    private Register soundTimer;

    /**
     * Construct a {@link CPUState}.
     */
    public CPUState() {
        memory = new MemoryBlock(MEMORY_SIZE, MEMORY_REGISTER_SIZE);
        stack = new MemoryBlock(STACK_SIZE, STACK_REGISTER_SIZE);
        stackPointer = new Register(STACK_POINTER_SIZE);
        vRegisters = new MemoryBlock(NUM_V_REGISTERS, V_REGISTER_SIZE);
        iRegister = new Register(I_REGISTER_SIZE);
        programCounter = new Register(PROGRAM_COUNTER_SIZE);
        delayTimer = new Register(DELAY_TIMER_SIZE);
        soundTimer = new Register(SOUND_TIMER_SIZE);
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
     * Set the SP register back and pop the value off the top.
     *
     * @return The popped value.
     */
    public int popStack() {
        if (isStackEmpty()) {
            throw new EmptyStackException();
        }

        stackPointer.set(stackPointer.read() - 1);
        return stack.read(stackPointer.read());
    }

    /**
     * Push a value to the top of the stack and move the SP register up.
     *
     * @param value The value to push to the stack.
     */
    public void pushStack(int value) {
        if (isStackFull()) {
            throw new FullStackException();
        }

        stack.set(stackPointer, value);
        stackPointer.set(stackPointer.read() + 1);
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
     * Read the value stored in the PC register.
     *
     * @return The value.
     */
    public int readPc() {
        return programCounter.read();
    }

    /**
     * Set the value stored in the PC register.
     *
     * @param value The value.
     */
    public void setPc(int value) {
        programCounter.set(value);
    }

    /**
     * Read the value stored in the delay timer register.
     *
     * @return The value.
     */
    public int readDt() {
        return delayTimer.read();
    }

    /**
     * Set the value stored in the delay timer register.
     *
     * @param value The value.
     */
    public void setDt(int value) {
        delayTimer.set(value);
    }

    /**
     * Read the value stored in the sound timer register.
     *
     * @return The value.
     */
    public int readSt() {
        return soundTimer.read();
    }

    /**
     * Set the value stored in the sound timer register.
     *
     * @param value The value.
     */
    public void setSt(int value) {
        soundTimer.set(value);
    }

    /**
     * @return True if the subroutine stack is empty.
     */
    private boolean isStackEmpty() {
        return stackPointer.read() == 0;
    }

    /**
     * @return True if the subroutine stack is full.
     */
    private boolean isStackFull() {
        return stackPointer.read() == stack.getSize();
    }
}
