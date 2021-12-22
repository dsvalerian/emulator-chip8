#ifndef DISASSEMBLER
#define DISASSEMBLER

#include <stdint.h>

typedef struct StateDisassembler {
    uint16_t pc;
    uint16_t buffer_size;
    uint8_t* buffer;
} StateDisassembler;

/**
 * @brief Disassembles the instruction at the program counter and moves to the next instruction.
 * 
 * @param state The disassembler state.
 */
void disassemble_instruction(StateDisassembler* state);

/**
 * @brief Disassembles a program stored in a StateDisassembler.
 * 
 * @param state The disassembler state.
 * 
 */
void disassemble_program(StateDisassembler* state);

/**
 * @brief Loads a binary file into a StateDisassembler.
 * 
 * @param state The disassembler state.
 * @param file_name Name of the file.
 */
void load_file_into_state(StateDisassembler* state, char* file_name);

/**
 * @brief Main function for disassembler.
 * 
 * @param argc Number of args.
 * @param argv Array of args.
 * @return int Return code.
 */
int main(int argc, char** argv);

#endif