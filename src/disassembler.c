#include <stdio.h>
#include <stdlib.h>

#include "disassembler.h"
#include "chip8.h"

void disassemble_instruction(uint16_t* buffer, uint16_t offset) {
    uint16_t op_code = *(buffer + offset);

    printf("0x%04x\t", op_code);

    switch(op_code & 0xf000) {
        case 0x0000: break;
            switch(op_code & 0x0fff) {
                case 0x0e0: printf("CLS"); break;
                case 0x0ee: printf("RET"); break;
            }
            break;
        case 0x1000: printf("JP\t0x%04x", op_code & 0xfff); break;
        case 0x2000: printf("CALL\t0x%04x", op_code & 0xfff); break;
        case 0x3000: printf("SE\tV%u,%u", (op_code & 0xf00) >> 8, op_code & 0xff); break;
        case 0x4000: printf("SNE\tV%u,%u", (op_code & 0xf00) >> 8, op_code & 0xff); break;
        case 0x5000: printf("SE\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
        case 0x6000: printf("LD\tV%u,%u", (op_code & 0xf00) >> 8, op_code & 0xff); break;
        case 0x7000: printf("ADD\tV%u,%u", (op_code & 0xf00) >> 8, op_code & 0xff); break;
        case 0x8000: 
            switch (op_code & 0xf) {
                case 0x0: printf("LD\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0x1: printf("OR\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0x2: printf("AND\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0x3: printf("XOR\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0x4: printf("ADD\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0x5: printf("SUB\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0x6: break;
                case 0x7: printf("SUBN\tV%u,V%u", (op_code & 0xf00) >> 8, (op_code & 0xf0) >> 4); break;
                case 0xe: break;
            }
            break;
        case 0x9000: break;
        case 0xa000: printf("LD\tI,0x%04x", op_code & 0xfff); break;
        case 0xb000: printf("JP\tV0,0x%04x", op_code & 0xfff);break;
        case 0xc000: printf("RND\tV%u,%u", (op_code & 0xf00) >> 8, op_code & 0xff); break;
        case 0xd000: break;
        case 0xe000: break;
        case 0xf000: break;
        
        default: 
            printf("Unrecognized OPCODE: 0x%04x", op_code);
            break;
    }

    printf("\n");
}

void disassemble_buffer(uint16_t* buffer) {
    pc = 0;
    while(pc < buffer_size) {
        disassemble_instruction(buffer, pc);
        pc += PC_STEP_SIZE;
    }
}

uint16_t* load_file_into_buffer(char* file_name) {
    FILE* rom_file = fopen(file_name, "r");

    if (rom_file == NULL) {
        printf("Error: Cannot open file: %s", file_name);
    }

    // Get the file size and read it into a memory buffer
    fseek(rom_file, 0L, SEEK_END);
    buffer_size = ftell(rom_file);
    fseek(rom_file, 0L, SEEK_SET);

    uint16_t* buffer = malloc(buffer_size);
    fread(buffer, buffer_size, 1, rom_file);
    fclose(rom_file);

    printf("Loaded file \'%s\', size: 0x%04x\n", file_name, buffer_size);

    return buffer;
}

int main(int argc, char** argv) {
    // Check for file
    if (argc <= 1) {
        printf("Error: No binary ROM provided.");
        exit(1);
    }  

    // Read through the file and disassemble every operation.
    uint16_t* buffer = load_file_into_buffer(argv[1]);  
    disassemble_buffer(buffer);

    return 0;
}