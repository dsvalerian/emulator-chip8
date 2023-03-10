package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPUState;
import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.FullStackException;

import java.util.EmptyStackException;

/**
 * {@inheritDoc}
 *
 * The default implementation of {@link CPUState}.
 */
public class Chip8CPUState implements CPUState {
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

    private int programSize;

    /**
     * Construct a {@link Chip8CPUState}.
     */
    public Chip8CPUState() {
        memory = new MemoryBlock(MEMORY_SIZE, MEMORY_REGISTER_SIZE);
        stack = new MemoryBlock(STACK_SIZE, STACK_REGISTER_SIZE);
        stackPointer = new Register(STACK_POINTER_SIZE);
        vRegisters = new MemoryBlock(NUM_V_REGISTERS, V_REGISTER_SIZE);
        iRegister = new Register(I_REGISTER_SIZE);
        programCounter = new Register(PROGRAM_COUNTER_SIZE);
        delayTimer = new Register(DELAY_TIMER_SIZE);
        soundTimer = new Register(SOUND_TIMER_SIZE);

        programSize = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readMemory(int address) {
        return memory.read(address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMemory(int address, int value) {
        memory.set(address, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int popStack() {
        if (isStackEmpty()) {
            throw new EmptyStackException();
        }

        stackPointer.set(stackPointer.read() - 1);
        return stack.read(stackPointer.read());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushStack(int value) {
        if (isStackFull()) {
            throw new FullStackException();
        }

        stack.set(stackPointer, value);
        stackPointer.set(stackPointer.read() + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readV(int x) {
        return vRegisters.read(x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setV(int x, int value) {
        vRegisters.set(x, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readI() {
        return iRegister.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setI(int value) {
        iRegister.set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readPc() {
        return programCounter.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPc(int value) {
        programCounter.set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readDt() {
        return delayTimer.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDt(int value) {
        delayTimer.set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readSt() {
        return soundTimer.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
