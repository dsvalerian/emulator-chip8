package com.github.dsvalerian.chip8;

import com.github.dsvalerian.chip8.data.MemoryBlock;
import com.github.dsvalerian.chip8.data.ROM;

public class CPU {
    private CPUProfile profile;
    private CPUState state;

    public CPU(CPUProfile profile) {
        this.profile = profile;
        this.state = new CPUState(profile);
    }

    public void loadProgram(ROM program) {
        loadMemory(profile.getProgramStartAddress(), program);
    }

    private void loadMemory(int address, MemoryBlock memory) {
        for (int i = 0; i < memory.getSize(); i++) {
            state.setMemory(address + i, memory.read(i));
        }
    }
}
