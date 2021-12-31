#include <stdlib.h>

#include "instructions.h"

void jp(StateChip8* state, uint16_t addr) {
    state->pc = addr - PC_STEP_SIZE;
}

void call(StateChip8* state, uint16_t addr) {
    (state->sp) += 1;
    *(state->sp) = state->pc;
    state->pc = addr - PC_STEP_SIZE;
}

void error_unhandled_instruction(StateChip8* state) {
    printf("Error: Unhandled instruction 0x%04x at $%03x.", state->last_opcode, state->pc);
    exit(1);
}