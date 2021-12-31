#include <stdlib.h>
#include <stdio.h>

#include "chip8.h"

StateChip8* state_chip8_new() {
    StateChip8* state = calloc(sizeof(StateChip8), 1);
    state->memory = malloc(MEMORY_SIZE);
    state->pc = PC_START;

    return state;
}

void state_chip8_delete(StateChip8** state) {
    free((*state)->memory);
    free(*state);
    *state = NULL;
}

void print_state(StateChip8* state) {
    /* ----- Basics ----- */
    printf("Last Opcode: 0x%04x --> {I: $%03x DT: %u ST: %u SP: %u PC: $%03x}\n", 
        state->last_opcode, state->i, state->dt, state->st, state->sp, state->pc);
    
    /* ----- V registers ----- */
    printf("  V[");

    int i;
    for (i = 0; i < NUM_REGISTERS; i++) {
        if (i != 0) {
            printf(" ");
        };

        printf("%u", state->v[i]);
    }

    printf("]");

    /* ----- Stack ----- */
    printf("  Stack: [");

    for (i = 0; i < NUM_REGISTERS; i++) {
        if (i != 0) {
            printf(" ");
        };

        printf("$%x", state->stack[i]);
    }

    printf("]\n");
}