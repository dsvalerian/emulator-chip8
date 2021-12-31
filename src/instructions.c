#include <stdlib.h>

#include "instructions.h"

void ret(StateChip8* state) {
    (state->sp)--;
    state->pc = state->stack[state->sp] - PC_STEP_SIZE;
    state->stack[state->sp] = 0;
}

void jp(StateChip8* state, uint16_t addr) {
    state->pc = addr - PC_STEP_SIZE;
}

void call(StateChip8* state, uint16_t addr) {
    state->stack[state->sp] = state->pc;
    (state->sp)++;
    state->pc = addr - PC_STEP_SIZE;
}

void se(StateChip8* state, uint8_t x, uint8_t byte) {
    if (state->v[x] == byte) {
        state->pc += PC_STEP_SIZE;
    }
}

void ld_vx_byte(StateChip8* state, uint8_t x, uint8_t byte) {
    state->v[x] = byte;
}

void add(StateChip8* state, uint8_t x, uint8_t byte) {
    state->v[x] += byte;
}

void ld_vx_vy(StateChip8* state, uint8_t x, uint8_t y) {
    state->v[x] = state->v[y];
}

void ld_i_addr(StateChip8* state, uint16_t addr) {
    state->i = addr;
}

void drw(StateChip8* state, uint8_t x, uint8_t y, uint8_t n) {
    uint8_t any_pixels_erased = 0;

    // Read through n bytes starting at I
    uint8_t byte_ptr;
    for (byte_ptr = 0; byte_ptr < n; byte_ptr++) {
        uint8_t byte = state->memory[state->i + byte_ptr];

        // Store bits big-endian
        uint8_t bits[8];
        bits[0] = (byte & 0b10000000) >> 7;
        bits[1] = (byte & 0b1000000) >> 6;
        bits[2] = (byte & 0b100000) >> 5;
        bits[3] = (byte & 0b10000) >> 4;
        bits[4] = (byte & 0b1000) >> 3;
        bits[5] = (byte & 0b100) >> 2;
        bits[6] = (byte & 0b10) >> 1;
        bits[7] = byte & 0b1;

        // Read through each bit and XOR a pixel in the display
        uint8_t bit_ptr;
        for (bit_ptr = 0; bit_ptr < 8; bit_ptr++) {
            // Get pixel pointer, keeping in mind display wrapping
            uint8_t* pixel = 
                &(state->display[(x + bit_ptr) % DISPLAY_WIDTH][(y + byte_ptr) % DISPLAY_HEIGHT]);
            uint8_t new_pixel = *pixel ^ bits[bit_ptr];

            // Check if pixel erased
            if (*pixel && !new_pixel) {
                any_pixels_erased = 1;
            }

            // Set new pixel
            *pixel = new_pixel;
        }
    }

    if (any_pixels_erased) {
        state->v[0xf] = 1;
    }
    else {
        state->v[0xf] = 0;
    }
}

void error_unhandled_instruction(StateChip8* state) {
    printf("Error: Unhandled instruction 0x%04x at $%03x.", state->last_opcode, state->pc);
    exit(1);
}