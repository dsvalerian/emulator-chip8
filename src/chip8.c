#include <stdlib.h>
#include <stdio.h>

#include "chip8.h"

StateChip8* state_chip8_new() {
    StateChip8* state = calloc(sizeof(StateChip8), 1);
    state->sp = calloc(STACK_SIZE, 2);
    print_state(state);
    state->memory = malloc(MEMORY_SIZE);
    state->pc = PC_START;

    return state;
}

void state_chip8_delete(StateChip8** state) {
    free((*state)->sp);
    free((*state)->memory);
    free(*state);
    *state = NULL;
}

void print_state(StateChip8* state) {
    /* ----- V registers ----- */
    printf("{ V[");

    int i;
    for (i = 0; i < NUM_REGISTERS; i++) {
        if (i != 0) {
            printf(" ");
        };

        printf("%u", state->v[i]);
    }

    printf("]\n");

    /* ----- Stack ----- */
    printf("  Stack: [");

    for (i = 0; i < STACK_SIZE; i++) {
        if (i != 0) {
            printf(" ");
        };

        printf("$%03x", state->sp[i]);
    }

    printf("]\n");

    /* ----- Remaining ----- */

    printf("  I: $%03x DT: %u ST: %u Program Size: 0x%03x PC: $%03x Last Opcode: 0x%04x }\n",
        state->i, state->dt, state->st, state->program_size, state->pc, state->last_opcode);
}