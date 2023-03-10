package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.Register;

/**
 * Representation of the full system. In charge of all the necessary parts for loading and
 * executing programs.
 */
public class CPU {
    private final Bits INSTRUCTION_BITS = Bits.SIXTEEN;
    private final int PC_STEP_SIZE = 2;
    private final int PROGRAM_START_ADDRESS = 0x200;

    private CPUState state;
    private Interpreter interpreter;

    private Register instructionBuffer;
    private ROM program;

    /**
     * Construct a {@link CPU}.
     */
    public CPU() {
        state = new CPUState();
        interpreter = new Interpreter(state, PC_STEP_SIZE);
        instructionBuffer = new Register(INSTRUCTION_BITS);
        program = ROM.fromEmpty();
    }

    /**
     * Read the next instruction and execute it.
     */
    public void processNextInstruction() {
        // Read and execute the next instruction. The state has the program and program counter already.
        loadNextInstruction();
        interpreter.executeInstruction(instructionBuffer);
    }

    /**
     * @return True if there are still more instructions in the loaded program for the CPU to process.
     */
    public boolean hasMoreInstructions() {
        return state.readPc() < PROGRAM_START_ADDRESS + program.getSize() &&
                state.readPc() >= PROGRAM_START_ADDRESS;
    }

    /**
     * Load a {@link ROM} into main memory and reset the program counter.
     *
     * @param program The {@link ROM} to load.
     */
    public void loadProgram(ROM program) {
        this.program = program;
        loadMemory(state, PROGRAM_START_ADDRESS, program);
        resetPc();
    }

    /**
     * Load the next program instruction into the instruction buffer.
     */
    private void loadNextInstruction() {
        int firstByteValue = state.readMemory(state.readPc());
        int secondByteValue = state.readMemory(state.readPc() + 1);

        instructionBuffer.set((firstByteValue << 8) + secondByteValue);
    }

    /**
     * Load a block of memory into the main memory of a {@link CPUState}, starting
     * at a specified address.
     *
     * @param state The {@link CPUState} into which the new {@link MemoryBlock} will be loaded.
     * @param address The address in the {@link CPUState}'s memory at which the
     *                new {@link MemoryBlock} will be loaded.
     * @param memory The {@link MemoryBlock} to load into the {@link CPUState}.
     */
    private void loadMemory(CPUState state, int address, MemoryBlock memory) {
        for (int i = 0; i < memory.getSize(); i++) {
            state.setMemory(address + i, memory.read(i));
        }
    }

    /**
     * Set the program counter to the program start address.
     */
    private void resetPc() {
        state.setPc(PROGRAM_START_ADDRESS);
    }
}
