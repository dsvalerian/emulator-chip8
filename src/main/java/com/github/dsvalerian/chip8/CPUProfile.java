package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Bits;
import com.github.dsvalerian.chip8.data.ROM;
import com.github.dsvalerian.chip8.data.Register;

/**
 * A representation of the layout of the CPU, with definitions of the memory size, stack size, number of Vx registers,
 * and number of bits used for each register and block of memory used by the CPU.
 */
public enum CPUProfile {
    /**
     * The profile used for the default Chip-8 implementation.
     */
    CHIP8(0x1000, Bits.EIGHT, 16, Bits.SIXTEEN, Bits.EIGHT,
            16, Bits.EIGHT, Bits.SIXTEEN, 0x200, Bits.SIXTEEN, Bits.TWELVE);

    private final int memorySize;
    private final Bits memoryRegisterBits;
    private final int stackSize;
    private final Bits stackRegisterBits;
    private final Bits spRegisterBits;
    private final int numVRegisters;
    private final Bits vRegisterBits;
    private final Bits iRegisterBits;
    private final Bits pcRegisterBits;
    private final int programStartAddress;
    private final Bits instructionBits;

    CPUProfile(int memorySize, Bits memoryRegisterBits,
               int stackSize, Bits stackRegisterBits, Bits spRegisterBits,
               int numVRegisters, Bits vRegisterBits,
               Bits pcRegisterBits, int programStartAddress,
               Bits instructionBits, Bits iRegisterBits) {
        this.memorySize = memorySize;
        this.memoryRegisterBits = memoryRegisterBits;
        this.stackSize = stackSize;
        this.stackRegisterBits = stackRegisterBits;
        this.spRegisterBits = spRegisterBits;
        this.numVRegisters = numVRegisters;
        this.vRegisterBits = vRegisterBits;
        this.pcRegisterBits = pcRegisterBits;
        this.programStartAddress = programStartAddress;
        this.instructionBits = instructionBits;
        this.iRegisterBits = iRegisterBits;
    }

    /**
     * @return The size of the main memory block.
     */
    public int getMemorySize() {
        return memorySize;
    }

    /**
     * @return The number of bits each {@link Register} in the main memory block can store.
     */
    public Bits getMemoryRegisterBits() {
        return memoryRegisterBits;
    }

    /**
     * @return The size of the subroutine stack.
     */
    public int getStackSize() {
        return stackSize;
    }

    /**
     * @return The number of bits each {@link Register} in the subroutine stack can store.
     */
    public Bits getStackRegisterBits() {
        return stackRegisterBits;
    }

    /**
     * @return The number of bits the stack pointer {@link Register} can store.
     */
    public Bits getSpRegisterBits() {
        return spRegisterBits;
    }

    /**
     * @return The number of Vx registers.
     */
    public int getNumVRegisters() {
        return numVRegisters;
    }

    /**
     * @return The number of bits each Vx {@link Register} can store.
     */
    public Bits getVRegisterBits() {
        return vRegisterBits;
    }

    /**
     * @return The number of bits the index {@link Register} can store.
     */
    public Bits getIRegisterBits() {
        return iRegisterBits;
    }

    /**
     * @return The number of bits the program counter {@link Register} can store.
     */
    public Bits getPcRegisterBits() {
        return pcRegisterBits;
    }

    /**
     * @return The address at which {@link ROM} programs should be loaded.
     */
    public int getProgramStartAddress() {
        return programStartAddress;
    }

    /**
     * @return The number of bits used for each instruction.
     */
    public Bits getInstructionBits() {
        return instructionBits;
    }
}
