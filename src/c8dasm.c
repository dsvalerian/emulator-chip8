#include <stdio.h>
#include <stdlib.h>

#include "c8dasm.h"
#include "chip8.h"
#include "instructions.h"

void print_opcode(uint16_t opcode) {
    printf("%02x %02x", (opcode & 0xff00) >> 8, opcode & 0xff);
}

void print_pc() {
    printf("0x%04x", pc);
}

void error_unrecognized_opcode(uint16_t opcode) {
    printf("Error: Unrecognized OPCODE ");
    print_opcode(opcode);
}

void disassemble_instruction(uint8_t* buffer, uint16_t offset) {
    uint16_t opcode = (uint16_t)*(buffer + offset) << 8 | (uint16_t)*(buffer + offset + 1);

    if (pc == buffer_size - 1) {
        opcode &= 0xff00;
    }

    // Potential values
    uint16_t nnn = opcode & 0xfff;
    uint8_t kk = opcode & 0xff;
    uint8_t n = opcode & 0xf;
    uint8_t x = (opcode & 0xf00) >> 8;
    uint8_t y = (opcode & 0xf0) >> 4;

    print_pc();
    printf(" ");
    print_opcode(opcode);
    printf("    ");

    switch(opcode & 0xf000) {
        case 0x0000:
            switch (opcode & 0x0fff) {
                case 0x0e0: printf(instructions[1].disassembly); break;         // 00E0
                case 0x0ee: printf(instructions[2].disassembly); break;         // 00EE
                default: printf(instructions[0].disassembly, nnn); break;       // 0nnn
            }
            break;
        case 0x1000: printf(instructions[3].disassembly, nnn); break;           // 1nnn
        case 0x2000: printf(instructions[4].disassembly, nnn); break;           // 2nnn
        case 0x3000: printf(instructions[5].disassembly, x, kk); break;         // 3xkk
        case 0x4000: printf(instructions[6].disassembly, x, kk); break;         // 4xkk
        case 0x5000: printf(instructions[7].disassembly, x, y); break;          // 5xy0
        case 0x6000: printf(instructions[8].disassembly, x, kk); break;         // 6xkk
        case 0x7000: printf(instructions[9].disassembly, x, kk); break;         // 7xkk
        case 0x8000: 
            switch (opcode & 0xf) {
                case 0x0: printf(instructions[10].disassembly, x, y); break;    // 8xy0
                case 0x1: printf(instructions[11].disassembly, x, y); break;    // 8xy1
                case 0x2: printf(instructions[12].disassembly, x, y); break;    // 8xy2
                case 0x3: printf(instructions[13].disassembly, x, y); break;    // 8xy3
                case 0x4: printf(instructions[14].disassembly, x, y); break;    // 8xy4
                case 0x5: printf(instructions[15].disassembly, x, y); break;    // 8xy5
                case 0x6: printf(instructions[16].disassembly, x); break;       // 8xy6
                case 0x7: printf(instructions[17].disassembly, x, y); break;    // 8xy7
                case 0xe: printf(instructions[18].disassembly, x); break;       // 8xy8
                default: error_unrecognized_opcode(opcode);
            }
            break;
        case 0x9000: printf(instructions[19].disassembly, x, y); break;         // 9xy0
        case 0xa000: printf(instructions[20].disassembly, nnn); break;          // Annn
        case 0xb000: printf(instructions[21].disassembly, nnn); break;          // Bnnn
        case 0xc000: printf(instructions[22].disassembly, x, kk); break;        // Cxkk
        case 0xd000: printf(instructions[23].disassembly, x, y, n); break;      // DxyN
        case 0xe000: 
            switch (opcode & 0xff) {
                case 0x9e: printf(instructions[24].disassembly, x); break;      // Ex9E
                case 0xa1: printf(instructions[25].disassembly, x); break;      // ExA1
                default: error_unrecognized_opcode(opcode); 
            }   
            break;  
        case 0xf000:    
            switch (opcode & 0xff) {    
                case 0x07: printf(instructions[26].disassembly, x); break;      // Fx07
                case 0x0a: printf(instructions[27].disassembly, x); break;      // Fx0A
                case 0x15: printf(instructions[28].disassembly, x); break;      // Fx15
                case 0x18: printf(instructions[29].disassembly, x); break;      // Fx18
                case 0x1e: printf(instructions[30].disassembly, x); break;      // Fx1E
                case 0x29: printf(instructions[31].disassembly, x); break;      // Fx29
                case 0x33: printf(instructions[32].disassembly, x); break;      // Fx33
                case 0x55: printf(instructions[33].disassembly, x); break;      // Fx55
                case 0x65: printf(instructions[34].disassembly, x); break;      // Fx65
                default: error_unrecognized_opcode(opcode);
            }
            break;
        default: error_unrecognized_opcode(opcode);
    }

    printf("\n");
}

void disassemble_buffer(uint8_t* buffer) {
    pc = PC_START;
    while(pc < buffer_size) {
        disassemble_instruction(buffer, pc);
        pc += PC_STEP_SIZE;
    }
}

uint8_t* load_file_into_buffer(char* file_name) {
    FILE* rom_file = fopen(file_name, "r");

    if (rom_file == NULL) {
        printf("Error: Cannot open file: %s", file_name);
    }

    // Get the file size and read it into a memory buffer
    fseek(rom_file, 0L, SEEK_END);
    int file_size = ftell(rom_file);
    fseek(rom_file, 0L, SEEK_SET);

    buffer_size = file_size + PC_START;

    uint8_t* buffer = malloc(buffer_size);
    fread(buffer + PC_START, buffer_size, 1, rom_file);
    fclose(rom_file);

    return buffer;
}

int main(int argc, char** argv) {
    // Check for file
    if (argc <= 1) {
        printf("Error: No binary ROM provided.");
        exit(1);
    }  

    // Read through the file and disassemble every operation.
    uint8_t* buffer = load_file_into_buffer(argv[1]);  
    disassemble_buffer(buffer);

    return 0;
}