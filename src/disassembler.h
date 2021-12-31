#ifndef DISASSEMBLER
#define DISASSEMBLER

#include <stdint.h>

#include "queue.h"

typedef struct StateDisassembler {
    uint16_t pc;
    uint16_t buffer_size;
    uint8_t* buffer;
} StateDisassembler;

/**
 * @brief Creates a new pointer to a StateDisassembler.
 * 
 * @return StateDisassembler* The new StateDisassembler.
 */
StateDisassembler* state_disassembler_new();

/**
 * @brief Frees/Deletes a StateDisassembler.
 * 
 * @param state Double pointer to the StateDisassembler to be deleted.
 */
void state_disassembler_delete(StateDisassembler** state);

/**
 * @brief Loads a binary file into a StateDisassembler.
 * 
 * @param state The disassembler state.
 * @param file_name Name of the file.
 */
void load_file_into_state(StateDisassembler* state, char* file_name);

/**
 * @brief Pre processes a program to construct labels and code segments. Without this step,
 * disassembling the program will result in invalid binary data being disassembled into code.
 * 
 * @param state Pointer to a StateDisassembler.
 * @param segments An empty Queue to be used for keeping track of segments.
 * @param codemap Buffer to be used for keeping track of valid code. Same size as state buffer.
 * @param labels Buffer to be used for keeping track of labels. Same size as state buffer.
 */
void pre_process_program(StateDisassembler* state, Queue* segments, uint8_t* codemap, 
    uint8_t* labels);

/**
 * @brief Disassembles a program stored in a StateDisassembler.
 * 
 * @param state The disassembler state.
 * @param codemap A buffer filled with valid code addresses from the pre-processor.
 * @param labels A buffer filled with valid labels from the pre-processor.
 */
void disassemble_program(StateDisassembler* state, uint8_t* codemap, uint8_t* labels);

#endif