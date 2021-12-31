#ifndef EMULATOR
#define EMULATOR

#include <stdio.h>

#include "chip8.h"

void emulate_program(StateChip8* state);
void load_file_into_state(StateChip8* state, char* filename);

void error_file_not_provided();
void error_file_access(char* filename);
void error_unrecognized_opcode(uint16_t opcode);

#endif