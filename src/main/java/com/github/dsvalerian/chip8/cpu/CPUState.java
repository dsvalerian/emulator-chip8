package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.StackEmptyException;
import com.github.dsvalerian.chip8.exception.StackFullException;

/**
 * Representation of the Chip-8 CPU state. Keeps track of all memory, registers, counters, delays, etc, and include
 * read and set methods for each.
 */
public class CPUState {
    /**
     * The size of main memory.
     */
    public static final int MEMORY_SIZE = 4096;
    /**
     * The number of bits used for each register in main memory.
     */
    public static final Bits MEMORY_REGISTER_SIZE = Bits.EIGHT;
    /**
     * The size of the program stack.
     */
    public static final int STACK_SIZE = 16;
    /**
     * The number of bits used for each register in the program stack.
     */
    public static final Bits STACK_REGISTER_SIZE = Bits.SIXTEEN;
    /**
     * The number of bits used by the stack pointer.
     */
    public static final Bits STACK_POINTER_SIZE = Bits.EIGHT;
    /**
     * The number of general-purpose V registers.
     */
    public static final int NUM_V_REGISTERS = 16;
    /**
     * The number of bits used by each general-purpose V register.
     */
    public static final Bits V_REGISTER_SIZE = Bits.EIGHT;
    /**
     * The number of bits used by the I index register.
     */
    public static final Bits I_REGISTER_SIZE = Bits.TWELVE;
    /**
     * The number of bits used by the program counter register.
     */
    public static final Bits PROGRAM_COUNTER_SIZE = Bits.SIXTEEN;
    /**
     * The number of bits used by the delay timer register.
     */
    public static final Bits DELAY_TIMER_SIZE = Bits.EIGHT;
    /**
     * The number of bits used by the sound timer register.
     */
    public static final Bits SOUND_TIMER_SIZE = Bits.EIGHT;

    private final MemoryBlock MEMORY = new MemoryBlock(MEMORY_SIZE, MEMORY_REGISTER_SIZE);
    private final MemoryBlock STACK = new MemoryBlock(STACK_SIZE, STACK_REGISTER_SIZE);
    private final Register STACK_POINTER = new Register(STACK_POINTER_SIZE);
    private final MemoryBlock V_REGISTERS = new MemoryBlock(NUM_V_REGISTERS, V_REGISTER_SIZE);
    private final Register I_REGISTER = new Register(I_REGISTER_SIZE);
    private final Register PROGRAM_COUNTER = new Register(PROGRAM_COUNTER_SIZE);
    private final Register DELAY_TIMER = new Register(DELAY_TIMER_SIZE);
    private final Register SOUND_TIMER = new Register(SOUND_TIMER_SIZE);
    private boolean paused = false;

    /**
     * Read the value at an address in main memory.
     *
     * @param address The address to read.
     * @return The value at the address.
     */
    public int readMemory(int address) {
        return MEMORY.read(address);
    }

    /**
     * Set a value at an address in main memory.
     *
     * @param address The address at which to set the value.
     * @param value The value to set.
     */
    public void setMemory(int address, int value) {
        MEMORY.set(address, value);
    }

    /**
     * Load a block of memory into the main memory, starting at a specified address.
     *
     * @param address The address in the memory at which the new {@link MemoryBlock} will be loaded.
     * @param memory The {@link MemoryBlock} to load into the memory.
     */
    public void loadMemory(int address, MemoryBlock memory) {
        for (int i = 0; i < memory.getSize(); i++) {
            setMemory(address + i, memory.read(i));
        }
    }

    /**
     * Set the SP register back and pop the value off the top.
     *
     * @return The popped value.
     */
    public int popStack() {
        if (isStackEmpty()) {
            throw new StackEmptyException();
        }

        STACK_POINTER.set(STACK_POINTER.read() - 1);
        return STACK.read(STACK_POINTER.read());
    }

    /**
     * Push a value to the top of the stack and move the SP register up.
     *
     * @param value The value to push to the stack.
     */
    public void pushStack(int value) {
        if (isStackFull()) {
            throw new StackFullException();
        }

        STACK.set(STACK_POINTER, value);
        STACK_POINTER.set(STACK_POINTER.read() + 1);
    }

    /**
     * Read the value stored in the Vx register, where x is the provided register number 0-15 inclusive.
     *
     * @param x The register number.
     * @return The value stored in the Vx register.
     */
    public int readV(int x) {
        return V_REGISTERS.read(x);
    }

    /**
     * Set the value in the Vx register, where x is the provided register number 0-15 inclusive.
     *
     * @param x The register number.
     * @param value The value to set.
     */
    public void setV(int x, int value) {
        V_REGISTERS.set(x, value);
    }

    /**
     * Read the value stored in the I register.
     *
     * @return The value.
     */
    public int readI() {
        return I_REGISTER.read();
    }

    /**
     * Set the value stored in the I register.
     *
     * @param value The value.
     */
    public void setI(int value) {
        I_REGISTER.set(value);
    }

    /**
     * Read the value stored in the PC register.
     *
     * @return The value.
     */
    public int readPc() {
        return PROGRAM_COUNTER.read();
    }

    /**
     * Set the value stored in the PC register.
     *
     * @param value The value.
     */
    public void setPc(int value) {
        PROGRAM_COUNTER.set(value);
    }

    /**
     * Read the value stored in the delay timer register.
     *
     * @return The value.
     */
    public int readDt() {
        return DELAY_TIMER.read();
    }

    /**
     * Set the value stored in the delay timer register.
     *
     * @param value The value.
     */
    public void setDt(int value) {
        DELAY_TIMER.set(value);
    }

    /**
     * Read the value stored in the sound timer register.
     *
     * @return The value.
     */
    public int readSt() {
        return SOUND_TIMER.read();
    }

    /**
     * Set the value stored in the sound timer register.
     *
     * @param value The value.
     */
    public void setSt(int value) {
        SOUND_TIMER.set(value);
    }

    /**
     * Set the state of execution to paused.
     */
    public void pause() {
        paused = true;
    }

    /**
     * Set the state of execution to not paused.
     */
    public void resume() {
        paused = false;
    }

    /**
     * @return True if the state of execution is paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return True if the subroutine stack is empty.
     */
    private boolean isStackEmpty() {
        return STACK_POINTER.read() == 0;
    }

    /**
     * @return True if the subroutine stack is full.
     */
    private boolean isStackFull() {
        return STACK_POINTER.read() == STACK.getSize();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("{")
                .append("PC: ").append(readPc()).append(", ")
                .append("V Registers: [");

        for (int i = 0; i < NUM_V_REGISTERS; i++) {
            builder.append(readV(i));

            if (i != NUM_V_REGISTERS - 1) {
                builder.append(", ");
            }
        }

        builder
                .append("], ")
                .append("I: ").append(readI()).append(", ")
                .append("SP: ").append(STACK_POINTER.read()).append(", ")
                .append("Stack: [");

        for (int i = 0; i < STACK_SIZE; i++) {
            builder.append(STACK.read(i));

            if (i != STACK_SIZE - 1) {
                builder.append(", ");
            }
        }

        builder
                .append("]")
                .append("}");

        return builder.toString();
    }
}
