package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPUProfile;
import com.github.dsvalerian.chip8.CPUState;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.exception.FullStackException;

import java.util.EmptyStackException;

/**
 * @inheritDocs
 *
 * The default implementation of {@link CPUState}.
 */
public class Chip8CPUState implements CPUState {
    private final CPUProfile PROFILE = CPUProfile.CHIP8;

    private MemoryBlock memory;
    private MemoryBlock stack;

    private MemoryBlock vRegisters;

    private Register iRegister;
    private Register pcRegister;
    private Register spRegister;

    private int programSize;

    /**
     * Construct a {@link Chip8CPUState}. Uses the default {@link CPUProfile}.
     */
    public Chip8CPUState() {
        memory = new MemoryBlock(PROFILE.getMemorySize(), PROFILE.getMemoryRegisterBits());
        stack = new MemoryBlock(PROFILE.getStackSize(), PROFILE.getStackRegisterBits());

        vRegisters = new MemoryBlock(PROFILE.getNumVRegisters(), PROFILE.getVRegisterBits());

        iRegister = new Register(PROFILE.getIRegisterBits());
        pcRegister = new Register(PROFILE.getPcRegisterBits());
        spRegister = new Register(PROFILE.getSpRegisterBits());

        programSize = 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int readMemory(int address) {
        return memory.read(address);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setMemory(int address, int value) {
        memory.set(address, value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int popStack() {
        if (isStackEmpty()) {
            throw new EmptyStackException();
        }

        spRegister.set(spRegister.read() - 1);
        return stack.read(spRegister.read());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void pushStack(int value) {
        if (isStackFull()) {
            throw new FullStackException();
        }

        stack.set(spRegister, value);
        spRegister.set(spRegister.read() + 1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int readV(int x) {
        return vRegisters.read(x);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setV(int x, int value) {
        vRegisters.set(x, value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int readI() {
        return iRegister.read();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setI(int value) {
        iRegister.set(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int readPc() {
        return pcRegister.read();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setPc(int value) {
        pcRegister.set(value);
    }

    /**
     * @return True if the subroutine stack is empty.
     */
    private boolean isStackEmpty() {
        return spRegister.read() == 0;
    }

    /**
     * @return True if the subroutine stack is full.
     */
    private boolean isStackFull() {
        return spRegister.read() == stack.getSize();
    }
}
