#ifndef C8DASM
#define C8DASM

#include <stdint.h>

int pc;
int buffer_size;

/**
 * @brief Disassembles the instruction at the location and prints it out.
 * 
 * @param buffer Memory buffer with binary instructions.
 * @param location Pointer to the instruction in the memory buffer.
 */
void disassemble_instruction(uint8_t* buffer, uint16_t offset);

/**
 * @brief Disassembles all binary instructions in a buffer and prints them out.
 * 
 * @param buffer Memory buffer with binary instructions.
 * 
 */
void disassemble_buffer(uint8_t* buffer);

/**
 * @brief Loads a binary file into a memory buffer.
 * 
 * @param file_name Name of the file.
 * @return uint16_t* Pointer to memory buffer.
 */
uint8_t* load_file_into_buffer(char* file_name);

/**
 * @brief Main function for disassembler.
 * 
 * @param argc Number of args.
 * @param argv Array of args.
 * @return int Return code.
 */
int main(int argc, char** argv);

#endif