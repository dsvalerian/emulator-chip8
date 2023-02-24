package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.Bits;

public enum CPUProfile {
    CHIP8(0x1000, Bits.EIGHT, 16, Bits.SIXTEEN, Bits.EIGHT,
            16, Bits.EIGHT, Bits.TWELVE, Bits.SIXTEEN, 0x200);

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

    CPUProfile(int memorySize, Bits memoryRegisterBits,
                       int stackSize, Bits stackRegisterBits, Bits spRegisterBits,
                       int numVRegisters, Bits vRegisterBits,
                       Bits iRegisterBits, Bits pcRegisterBits, int programStartAddress) {
        this.memorySize = memorySize;
        this.memoryRegisterBits = memoryRegisterBits;
        this.stackSize = stackSize;
        this.stackRegisterBits = stackRegisterBits;
        this.spRegisterBits = spRegisterBits;
        this.numVRegisters = numVRegisters;
        this.vRegisterBits = vRegisterBits;
        this.iRegisterBits = iRegisterBits;
        this.pcRegisterBits = pcRegisterBits;
        this.programStartAddress = programStartAddress;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public Bits getMemoryRegisterBits() {
        return memoryRegisterBits;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Bits getStackRegisterBits() {
        return stackRegisterBits;
    }

    public Bits getSpRegisterBits() {
        return spRegisterBits;
    }

    public int getNumVRegisters() {
        return numVRegisters;
    }

    public Bits getVRegisterBits() {
        return vRegisterBits;
    }

    public Bits getIRegisterBits() {
        return iRegisterBits;
    }

    public Bits getPcRegisterBits() {
        return pcRegisterBits;
    }

    public int getProgramStartAddress() {
        return programStartAddress;
    }
}
