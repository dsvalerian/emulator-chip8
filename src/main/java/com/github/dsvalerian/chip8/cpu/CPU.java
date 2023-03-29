package com.github.dsvalerian.chip8.cpu;

import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.data.Sprites;
import com.github.dsvalerian.chip8.exception.NoProgramLoadedException;
import com.github.dsvalerian.chip8.io.Keyboard;
import com.github.dsvalerian.chip8.io.Screen;

/**
 * Representation of the Chip-8 CPU. In charge of all the necessary parts for loading and
 * executing programs.
 */
public class CPU {
    private static final int PROGRAM_START_ADDRESS = 0x200;
    private static final CPUSpeed SPEED = CPUSpeed.FULL;

    private CPUState state;
    private CPUTimings timings;
    private Interpreter interpreter;

    private Register instructionBuffer;
    private ROM program;

    /**
     * Create a new {@link CPU}.
     * @param state Represents the state of the CPU.
     * @param timings Handles time-related CPU tasks.
     * @param screen Represents the system screen.
     * @param keyboard Represents the state of the keyboard.
     */
    public CPU(CPUState state, CPUTimings timings, Screen screen, Keyboard keyboard) {
        this.state = state;
        this.timings = timings;
        this.interpreter = new Interpreter(state, screen, keyboard);

        instructionBuffer = new Register(Interpreter.INSTRUCTION_BITS);
        program = null;
        Sprites.load(state);
    }

    /**
     * Read the next instruction and execute it.
     */
    public void processNextInstruction() {
        if (program == null) {
            throw new NoProgramLoadedException();
        }

        // Read and execute the next instruction. The state has the program and program counter already.
        if (!state.isPaused()) {
            long startTime = System.nanoTime();

            loadNextInstruction();
            interpreter.executeInstruction(instructionBuffer);

            timings.finishCycle(startTime);
            timings.handleTimers();
        }
    }

    /**
     * @return True if there are still more instructions in the loaded program for the CPU to process.
     */
    public boolean hasMoreInstructions() {
        if (program == null) {
            throw new NoProgramLoadedException();
        }

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
