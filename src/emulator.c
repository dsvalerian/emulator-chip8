#define SDL_MAIN_HANDLED

#include <stdio.h>
#include <stdlib.h>

#include "emulator.h"
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
                case 0x0e0:     // 00E0
                    ((void (*)(StateChip8*))instructions[1].func)(state); 
                    break;
                case 0x0ee:     // 00EE
                    ((void (*)(StateChip8*))instructions[2].func)(state); 
                    break;     
                default:        // 0nnn
                    ((void (*)(StateChip8*))instructions[0].func)(state); 
                    break;
            }
            break;
        case 0x1000:            // 1nnn
            ((void (*)(StateChip8*, uint16_t))instructions[3].func)(state, nnn); 
            break;           
        case 0x2000:            // 2nnn
            ((void (*)(StateChip8*, uint16_t))instructions[4].func)(state, nnn); 
            break;           
        case 0x3000:            // 3xkk
            ((void (*)(StateChip8*, uint8_t, uint8_t))instructions[5].func)(state, x, kk); 
            break;           
        case 0x4000:            // 4xkk
            ((void (*)(StateChip8*))instructions[6].func)(state); 
            break;           
        case 0x5000:            // 5xy0
            ((void (*)(StateChip8*))instructions[7].func)(state); 
            break;           
        case 0x6000:            // 6xkk
            ((void (*)(StateChip8*, uint8_t, uint8_t))instructions[8].func)(state, x, kk); 
            break;
        case 0x7000:            // 7xkk
            ((void (*)(StateChip8*, uint8_t, uint8_t))instructions[9].func)(state, x, kk); 
            break;           
        case 0x8000: 
            switch (opcode & 0xf) {
                case 0x0:       // 8xy0
                    ((void (*)(StateChip8*, uint8_t, uint8_t))instructions[10].func)(state, x, y); 
                    break;     
                case 0x1:       // 8xy1
                    ((void (*)(StateChip8*))instructions[11].func)(state); 
                    break;     
                case 0x2:       // 8xy2
                    ((void (*)(StateChip8*))instructions[12].func)(state); 
                    break;     
                case 0x3:       // 8xy3
                    ((void (*)(StateChip8*))instructions[13].func)(state); 
                    break;     
                case 0x4:       // 8xy4
                    ((void (*)(StateChip8*))instructions[14].func)(state); 
                    break;     
                case 0x5:       // 8xy5
                    ((void (*)(StateChip8*))instructions[15].func)(state); 
                    break;     
                case 0x6:       // 8xy6
                    ((void (*)(StateChip8*))instructions[16].func)(state); 
                    break;     
                case 0x7:       // 8xy7
                    ((void (*)(StateChip8*))instructions[17].func)(state); 
                    break;     
                case 0xe:       // 8xy8
                    ((void (*)(StateChip8*))instructions[18].func)(state); 
                    break;     
                default: error_unrecognized_opcode(opcode); 
            }   
            break;  
        case 0x9000:            // 9xy0
            ((void (*)(StateChip8*))instructions[19].func)(state); 
            break;          
        case 0xa000:            // Annn
            ((void (*)(StateChip8*, uint16_t))instructions[20].func)(state, nnn); 
            break;          
        case 0xb000:            // Bnnn
            ((void (*)(StateChip8*))instructions[21].func)(state); 
            break;          
        case 0xc000:            // Cxkk
            ((void (*)(StateChip8*))instructions[22].func)(state); 
            break;          
        case 0xd000:            // DxyN
            ((void (*)(StateChip8*, uint8_t, uint8_t, uint8_t))instructions[23].func)(state, x, y, n); 
            break;          
        case 0xe000: 
            switch (opcode & 0xff) {
                case 0x9e:      // Ex9E
                    ((void (*)(StateChip8*))instructions[24].func)(state); 
                    break;    
                case 0xa1:      // ExA1
                    ((void (*)(StateChip8*))instructions[25].func)(state); 
                    break;    
                default: error_unrecognized_opcode(opcode); 
            }   
            break;  
        case 0xf000:    
            switch (opcode & 0xff) {    
                case 0x07:       // Fx07
                    ((void (*)(StateChip8*))instructions[26].func)(state); 
                    break;    
                case 0x0a:      // Fx0A
                    ((void (*)(StateChip8*))instructions[27].func)(state); 
                    break;    
                case 0x15:      // Fx15
                    ((void (*)(StateChip8*))instructions[28].func)(state); 
                    break;    
                case 0x18:      // Fx18
                    ((void (*)(StateChip8*))instructions[29].func)(state); 
                    break;    
                case 0x1e:      // Fx1E
                    ((void (*)(StateChip8*))instructions[30].func)(state); 
                    break;    
                case 0x29:      // Fx29
                    ((void (*)(StateChip8*))instructions[31].func)(state); 
                    break;    
                case 0x33:      // Fx33
                    ((void (*)(StateChip8*))instructions[32].func)(state); 
                    break;    
                case 0x55:      // Fx55
                    ((void (*)(StateChip8*))instructions[33].func)(state); 
                    break;    
                case 0x65:      // Fx65
                    ((void (*)(StateChip8*))instructions[34].func)(state); 
                    break;    
                default: error_unrecognized_opcode(opcode);
            }
            break;
        default: error_unrecognized_opcode(opcode);
    }
}

void emulate_program(StateChip8* state, Screen* screen) {
    state->pc = PC_START;

    // TODO delete this for full emulation
    unsigned int instruction_count = 0;

    while (state->pc < state->program_size + PC_START) {
        // TODO delete this for full emulation
        if (instruction_count >= 1000) {
            break;
        }
        //if (instruction_count % 1000 == 0) {
            printf("Frame %u\n", instruction_count);
        //}

        prepare_scene(screen);

        emulate_instruction(state);
        state->pc += 2;

        present_scene(screen);

        SDL_Delay(17);

        // TODO Delete this for full emulation
        instruction_count++;
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
    Screen* screen = screen_new();
    StateChip8* state = state_chip8_new();
    load_file_into_state(state, argv[1]);
    printf("Loaded ROM: %s\n", argv[1]);

    /* ----- Emulation ----- */
    emulate_program(state, screen);

    /* ----- Clean up ----- */
    screen_delete(&screen);
    state_chip8_delete(&state);
}