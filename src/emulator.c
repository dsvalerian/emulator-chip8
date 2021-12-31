#include <stdio.h>
#include <stdlib.h>

#include "emulator.h"
#include "chip8.h"
#include "instructions.h"

uint16_t get_opcode(StateChip8* state) {
    return (uint16_t)state->memory[state->pc] << 8 | (uint16_t)state->memory[state->pc + 1];
}

void emulate_instruction(StateChip8* state) {
    uint16_t opcode = get_opcode(state);
    state->last_opcode = opcode;

    // Values that may potentially be useful, depending on the instruction.
    uint16_t nnn = opcode & 0xfff;
    uint8_t kk = opcode & 0xff;
    uint8_t n = opcode & 0xf;
    uint8_t x = (opcode & 0xf00) >> 8;
    uint8_t y = (opcode & 0xf0) >> 4;

    // Main switch statement, handles every CHIP-8 instruction.
    switch (opcode & 0xf000) {
        case 0x0000:
            switch (opcode & 0x0fff) {
                case 0x0e0: ((void (*)(StateChip8*))instructions[1].func)(state); break;    // 00E0
                case 0x0ee:((void (*)(StateChip8*))instructions[2].func)(state); break;     // 00EE
                default: ((void (*)(StateChip8*))instructions[0].func)(state); break;       // 0nnn
            }
            break;
        case 0x1000: ((void (*)(StateChip8*, uint16_t))instructions[3].func)(state, nnn); break;           // 1nnn
        case 0x2000: ((void (*)(StateChip8*, uint16_t))instructions[4].func)(state, nnn); break;           // 2nnn
        case 0x3000: ((void (*)(StateChip8*))instructions[5].func)(state); break;           // 3xkk
        case 0x4000: ((void (*)(StateChip8*))instructions[6].func)(state); break;           // 4xkk
        case 0x5000: ((void (*)(StateChip8*))instructions[7].func)(state); break;           // 5xy0
        case 0x6000: ((void (*)(StateChip8*))instructions[8].func)(state); break;           // 6xkk
        case 0x7000: ((void (*)(StateChip8*))instructions[9].func)(state); break;           // 7xkk
        case 0x8000: 
            switch (opcode & 0xf) {
                case 0x0: ((void (*)(StateChip8*))instructions[10].func)(state); break;     // 8xy0
                case 0x1: ((void (*)(StateChip8*))instructions[11].func)(state); break;     // 8xy1
                case 0x2: ((void (*)(StateChip8*))instructions[12].func)(state); break;     // 8xy2
                case 0x3: ((void (*)(StateChip8*))instructions[13].func)(state); break;     // 8xy3
                case 0x4: ((void (*)(StateChip8*))instructions[14].func)(state); break;     // 8xy4
                case 0x5: ((void (*)(StateChip8*))instructions[15].func)(state); break;     // 8xy5
                case 0x6: ((void (*)(StateChip8*))instructions[16].func)(state); break;     // 8xy6
                case 0x7: ((void (*)(StateChip8*))instructions[17].func)(state); break;     // 8xy7
                case 0xe: ((void (*)(StateChip8*))instructions[18].func)(state); break;     // 8xy8
                default: error_unrecognized_opcode(opcode); 
            }   
            break;  
        case 0x9000: ((void (*)(StateChip8*))instructions[19].func)(state); break;          // 9xy0
        case 0xa000: ((void (*)(StateChip8*))instructions[20].func)(state); break;          // Annn
            break;  
        case 0xb000: ((void (*)(StateChip8*))instructions[21].func)(state); break;          // Bnnn
        case 0xc000: ((void (*)(StateChip8*))instructions[22].func)(state); break;          // Cxkk
        case 0xd000: ((void (*)(StateChip8*))instructions[23].func)(state); break;          // DxyN
        case 0xe000: 
            switch (opcode & 0xff) {
                case 0x9e: ((void (*)(StateChip8*))instructions[24].func)(state); break;    // Ex9E
                case 0xa1: ((void (*)(StateChip8*))instructions[25].func)(state); break;    // ExA1
                default: error_unrecognized_opcode(opcode); 
            }   
            break;  
        case 0xf000:    
            switch (opcode & 0xff) {    
                case 0x07: ((void (*)(StateChip8*))instructions[26].func)(state); break;    // Fx07
                case 0x0a: ((void (*)(StateChip8*))instructions[27].func)(state); break;    // Fx0A
                case 0x15: ((void (*)(StateChip8*))instructions[28].func)(state); break;    // Fx15
                case 0x18: ((void (*)(StateChip8*))instructions[29].func)(state); break;    // Fx18
                case 0x1e: ((void (*)(StateChip8*))instructions[30].func)(state); break;    // Fx1E
                case 0x29: ((void (*)(StateChip8*))instructions[31].func)(state); break;    // Fx29
                case 0x33: ((void (*)(StateChip8*))instructions[32].func)(state); break;    // Fx33
                case 0x55: ((void (*)(StateChip8*))instructions[33].func)(state); break;    // Fx55
                case 0x65: ((void (*)(StateChip8*))instructions[34].func)(state); break;    // Fx65
                default: error_unrecognized_opcode(opcode);
            }
            break;
        default: error_unrecognized_opcode(opcode);
    }
}

void emulate_program(StateChip8* state) {
    state->pc = PC_START;
    print_state(state);

    while (state->pc < state->program_size + PC_START) {
        emulate_instruction(state);
        state->pc += 2;

        print_state(state);
    }
}

void load_file_into_state(StateChip8* state, char* filename) {
    FILE* rom_file = fopen(filename, "r");

    if (rom_file == NULL) {
        error_file_access(filename);
    }

    // Get the file size and read it into a memory buffer
    fseek(rom_file, 0L, SEEK_END);
    state->program_size = ftell(rom_file);
    fseek(rom_file, 0L, SEEK_SET);

    fread(state->memory + PC_START, state->program_size, 1, rom_file);
    fclose(rom_file);
}

void error_file_not_provided() {
    printf("Error: File not provided.\n");
    exit(1);
}

void error_file_access(char* filename) {
    printf("Error: Cannot open file: %s\n", filename);
    exit(1);
}

void error_unrecognized_opcode(uint16_t opcode) {
    printf("Error: Unrecognized opcode: 0x%04x\n", opcode);
    exit(1);
}

int main(int argc, char** argv) {
    if (argc < 2) {
        error_file_not_provided();
    }

    /* ----- Initialization ----- */
    StateChip8* state = state_chip8_new();
    load_file_into_state(state, argv[1]);
    printf("Loaded ROM: %s\n", argv[1]);

    /* ----- Emulation ----- */
    emulate_program(state);

    /* ----- Clean up ----- */
    state_chip8_delete(&state);
}