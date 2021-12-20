#ifndef CHIP8
#define CHIP8

#include <stdint.h>

#define NUM_REGISTERS 16
#define PC_STEP_SIZE 2
#define MEMORY_SIZE 0x1000
#define PC_START 0x200

typedef struct Chip8 {
    // Registers
    uint8_t v[NUM_REGISTERS];
    
} Chip8;

#endif