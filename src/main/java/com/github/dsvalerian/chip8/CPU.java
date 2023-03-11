package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.Register;

/**
 * Representation of the Chip-8 CPU. In charge of all the necessary parts for loading and
 * executing programs.
 */
public class CPU {
    private final int PROGRAM_START_ADDRESS = 0x200;

    private CPUState state;
    private Interpreter interpreter;
    private Screen screen;

    private Register instructionBuffer;
    private ROM program;

    /**
     * Construct a {@link CPU}.
     *
     * @param screen The {@link Screen} used by the system.
     */
    public CPU(Screen screen) {
        state = new CPUState();
        interpreter = new Interpreter(state, screen);
        this.screen = screen;
        instructionBuffer = new Register(Interpreter.INSTRUCTION_BITS);
        program = ROM.fromEmpty();

        // Load sprites into main memory.
        Sprites.load(state);
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
        state.loadMemory(PROGRAM_START_ADDRESS, program);
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
     * Set the program counter to the program start address.
     */
    private void resetPc() {
        state.setPc(PROGRAM_START_ADDRESS);
    }
}
