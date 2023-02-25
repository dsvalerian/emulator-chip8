package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.Register;
import com.github.dsvalerian.chip8.util.Constants;

/**
 * Representation of the Chip-8 CPU. This class is in charge of stepping through programs
 * and delegating work (such as executing instructions and keeping track of state) to other
 * classes.
 */
public class CPU {
    private CPUProfile profile = CPUProfile.CHIP8;
    private CPUState state;
    private Register instruction;

    public CPU() {
        this.state = new CPUState(profile);
        instruction = new Register(Constants.CHIP8_INSTRUCTION_BITS);
    }

    /**
     * Read the next instruction and execute it.
     */
    public void processNextInstruction() {
        // Read and execute the instruction at the program counter.
        // Instructions are in charge of incrementing the program counter, so we don't do it here.
        Instructions.load(state, instruction);
        Instructions.execute(state, instruction);
    }

    /**
     * @return True if there are still more instructions in the loaded program for the CPU to process.
     */
    public boolean hasMoreInstructions() {
        return state.readPc() < profile.getProgramStartAddress() + state.getProgramSize() &&
                state.readPc() >= profile.getProgramStartAddress();
    }

    /**
     * Load a {@link ROM} into main memory and reset the program counter.
     *
     * @param program The {@link ROM} to load.
     */
    public void loadProgram(ROM program) {
        loadMemory(state, profile.getProgramStartAddress(), program);
        state.setProgramSize(program.getSize());
        resetPc();
    }

    /**
     * Set the program counter to the program start address.
     */
    private void resetPc() {
        state.setPc(profile.getProgramStartAddress());
    }

    /**
     * Load a block of memory into the main memory of a {@link CPUState}, starting
     * at a specified address.
     *
     * @param state The {@link CPUState} into which the new {@link MemoryBlock} will be loaded.
     * @param address The address in the {@link CPUState}'s memory at which the new {@link MemoryBlock} will be loaded.
     * @param memory The {@link MemoryBlock} to load into the {@link CPUState}.
     */
    private void loadMemory(CPUState state, int address, MemoryBlock memory) {
        for (int i = 0; i < memory.getSize(); i++) {
            state.setMemory(address + i, memory.read(i));
        }
    }
}
