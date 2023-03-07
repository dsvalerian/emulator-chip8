package com.github.dsvalerian.chip8.impl;

import com.github.dsvalerian.chip8.CPU;
import com.github.dsvalerian.chip8.CPUProfile;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.util.Constants;

/**
 * {@inheritDoc}
 *
 * The default implementation of {@link CPU}.
 */
public class Chip8CPU implements CPU {
    private final CPUProfile PROFILE = CPUProfile.CHIP8;
    private final int PC_STEP_SIZE = PROFILE.getInstructionBits().getValue() / Constants.BYTE;

    private Chip8CPUState state;
    private Chip8Interpreter interpreter;

    private Register instructionBuffer;
    private ROM program;

    /**
     * Construct a {@link Chip8CPU}. Uses the default {@link CPUProfile}.
     */
    public Chip8CPU() {
        state = new Chip8CPUState();
        interpreter = new Chip8Interpreter(state);
        instructionBuffer = new Register(PROFILE.getInstructionBits());
        program = ROM.fromEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public void processNextInstruction() {
        // Read and execute the next instruction. The state has the program and program counter already.
        loadNextInstruction();
        interpreter.executeInstruction(instructionBuffer);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasMoreInstructions() {
        return state.readPc() < PROFILE.getProgramStartAddress() + program.getSize() &&
                state.readPc() >= PROFILE.getProgramStartAddress();
    }

    /**
     * {@inheritDoc}
     */
    public void loadProgram(ROM program) {
        this.program = program;
        loadMemory(state, PROFILE.getProgramStartAddress(), program);
        resetPc();
    }

    /**
     * Load the next program instruction into the instruction buffer.
     */
    private void loadNextInstruction() {
        int instructionValue = 0;

        for (int i = 0; i < PC_STEP_SIZE; i++) {
            // Big endian, so bit shift the first values more, and the last isn't shifted at all.
            int byteValue = state.readMemory(state.readPc() + i);
            byteValue = (byteValue << (PC_STEP_SIZE - i - 1)) * Constants.BYTE;
            instructionValue += byteValue;
        }

        instructionBuffer.set(instructionValue);
    }

    /**
     * Load a block of memory into the main memory of a {@link Chip8CPUState}, starting
     * at a specified address.
     *
     * @param state The {@link Chip8CPUState} into which the new {@link MemoryBlock} will be loaded.
     * @param address The address in the {@link Chip8CPUState}'s memory at which the
     *                new {@link MemoryBlock} will be loaded.
     * @param memory The {@link MemoryBlock} to load into the {@link Chip8CPUState}.
     */
    private void loadMemory(Chip8CPUState state, int address, MemoryBlock memory) {
        for (int i = 0; i < memory.getSize(); i++) {
            state.setMemory(address + i, memory.read(i));
        }
    }

    /**
     * Set the program counter to the program start address.
     */
    private void resetPc() {
        state.setPc(PROFILE.getProgramStartAddress());
    }
}
