#include <stdio.h>
#include <stdlib.h>

#include "disassembler.h"
#include "chip8.h"
#include "instructions.h"

uint16_t get_opcode(StateDisassembler* state) {
    uint8_t* opbytes = &(state->buffer[state->pc]);
    uint16_t opcode;

    // If there's enough data in the buffer, get two bytes. Otherwise, just one.
    if (state->pc == state->buffer_size - 1) {
        opcode = (uint16_t)opbytes[0] << 8;
    }
    else {
        opcode = (uint16_t)opbytes[0] << 8 | (uint16_t)opbytes[1];
    }

    return opcode;
}

void print_metadata(StateDisassembler* state) {
    uint16_t opcode = get_opcode(state);
    printf("0x%03x  %02x %02x", state->pc, (opcode & 0xff00) >> 8, opcode & 0xff);
}

void error_unrecognized_opcode(uint16_t opcode) {
    printf("Error: Unrecognized OPCODE 0x%04x", opcode);
}

void print_decoded_text(char* buffer, unsigned int* last_index) {
    buffer[*last_index] = '\0';
    printf("\n; Decoded Text: %s", buffer);

    *last_index = 0;
}

StateDisassembler* state_disassembler_new() {
    StateDisassembler* state = malloc(sizeof(StateDisassembler));

    return state;
}

void state_disassembler_delete(StateDisassembler** state) {
    free((*state)->buffer);
    free(*state);
    *state = NULL;
}

void load_file_into_state_disassembler(StateDisassembler* state, char* file_name) {
    FILE* rom_file = fopen(file_name, "r");

    if (rom_file == NULL) {
        printf("Error: Cannot open file: %s", file_name);
    }

    // Get the file size and read it into a memory buffer
    fseek(rom_file, 0L, SEEK_END);
    int file_size = ftell(rom_file);
    fseek(rom_file, 0L, SEEK_SET);

    state->buffer_size = file_size + PC_START;

    state->buffer = malloc(state->buffer_size);
    fread(state->buffer + PC_START, state->buffer_size, 1, rom_file);
    fclose(rom_file);
}

void pre_process_program(StateDisassembler* state, Queue* segments, uint8_t* codemap, uint8_t* labels) {
    state->pc = PC_START;

    queue_push(segments, state->pc);

    // While there are segments to be processed
    while (!queue_empty(segments)) {
        state->pc = queue_pop(segments);

        // Walking through this current segment of the code
        while (state->pc < state->buffer_size && !codemap[state->pc]) {
            // Marking this address as actual code
            codemap[state->pc] = 1;

            // Get the current opcode
            uint16_t opcode = get_opcode(state);

            // Move to the next op
            state->pc += PC_STEP_SIZE;

            // Check the current opcode
            switch (opcode & 0xf000) {
                // Jumping to an address so we mark the destination as a label
                case 0x1000:        // JP addr
                    state->pc = opcode & 0xfff;
                    labels[state->pc] = 1;
                    break;

                // Calling, current pc is start of a segment, destination is a label
                case 0x2000:        // CALL addr
                    queue_push(segments, state->pc);
                    state->pc = opcode & 0xfff;
                    labels[opcode & 0xfff] = 1;
                    break;
                
                // Skipping instruction, so we save the next segment as something to check
                case 0x3000:        // SE    Vx, byte
                case 0x4000:        // SNE   Vx, byte
                case 0x5000:        // SE    Vx, Vy
                case 0x9000:        // SNE   Vx, Vy
                    queue_push(segments, state->pc + 2);
                    break;
                case 0xe000:
                    switch (opcode & 0xff) {
                        case 0x9E:  // SKP   Vx
                        case 0xa1:  // SKNP  Vy
                            queue_push(segments, state->pc + 2);
                            break;
                    }
                    break;

                // V0 could contain anything, we won't know until runtime. So we can't disassemble.
                case 0xb000:        // JP    V0, addr
                    printf("Error: Encountered \'");
                    printf(instructions[21].disassembly, (state->pc - 2) & 0xfff);
                    printf("\' instruction at 0x%03x.\n", state->pc - 2);
                    printf("Cannot proceed with disassembly.\n");
                    break;
            }
        }
    }
}

void disassemble_instruction(StateDisassembler* state, uint8_t* labels, char lazy) {
    uint16_t opcode = get_opcode(state);

    // Values that may potentially be printed, depending on the instruction.
    uint16_t nnn = opcode & 0xfff;
    uint8_t kk = opcode & 0xff;
    uint8_t n = opcode & 0xf;
    uint8_t x = (opcode & 0xf00) >> 8;
    uint8_t y = (opcode & 0xf0) >> 4;

    // Main switch statement, handles every CHIP-8 instruction.
    switch (opcode & 0xf000) {
        case 0x0000:
            switch (opcode & 0x0fff) {
                case 0x0e0: printf(instructions[1].disassembly); break;         // 00E0
                case 0x0ee: printf(instructions[2].disassembly); break;         // 00EE
                default: printf(instructions[0].disassembly, nnn); break;       // 0nnn
            }
            break;
        case 0x1000:                                                            // 1nnn
            if (lazy)
                printf(instructions[3].lazy_disassembly, nnn);
            else
                printf(instructions[3].disassembly, nnn); 
            break;       
        case 0x2000:                                                            // 2nnn
            if (lazy)
                printf(instructions[4].lazy_disassembly, nnn);
            else
                printf(instructions[4].disassembly, nnn); 
            break;  
        case 0x3000: printf(instructions[5].disassembly, x, kk); break;         // 3xkk
        case 0x4000: printf(instructions[6].disassembly, x, kk); break;         // 4xkk
        case 0x5000: printf(instructions[7].disassembly, x, y); break;          // 5xy0
        case 0x6000: printf(instructions[8].disassembly, x, kk); break;         // 6xkk
        case 0x7000: printf(instructions[9].disassembly, x, kk); break;         // 7xkk
        case 0x8000: 
            switch (opcode & 0xf) {
                case 0x0: printf(instructions[10].disassembly, x, y); break;    // 8xy0
                case 0x1: printf(instructions[11].disassembly, x, y); break;    // 8xy1
                case 0x2: printf(instructions[12].disassembly, x, y); break;    // 8xy2
                case 0x3: printf(instructions[13].disassembly, x, y); break;    // 8xy3
                case 0x4: printf(instructions[14].disassembly, x, y); break;    // 8xy4
                case 0x5: printf(instructions[15].disassembly, x, y); break;    // 8xy5
                case 0x6: printf(instructions[16].disassembly, x); break;       // 8xy6
                case 0x7: printf(instructions[17].disassembly, x, y); break;    // 8xy7
                case 0xe: printf(instructions[18].disassembly, x); break;       // 8xy8
                default: error_unrecognized_opcode(opcode);
            }
            break;
        case 0x9000: printf(instructions[19].disassembly, x, y); break;         // 9xy0
        case 0xa000: printf(instructions[20].disassembly, nnn); break;          // Annn
            break;  
        case 0xb000: printf(instructions[21].disassembly, nnn); break;          // Bnnn
        case 0xc000: printf(instructions[22].disassembly, x, kk); break;        // Cxkk
        case 0xd000: printf(instructions[23].disassembly, x, y, n); break;      // DxyN
        case 0xe000: 
            switch (opcode & 0xff) {
                case 0x9e: printf(instructions[24].disassembly, x); break;      // Ex9E
                case 0xa1: printf(instructions[25].disassembly, x); break;      // ExA1
                default: error_unrecognized_opcode(opcode); 
            }   
            break;  
        case 0xf000:    
            switch (opcode & 0xff) {    
                case 0x07: printf(instructions[26].disassembly, x); break;      // Fx07
                case 0x0a: printf(instructions[27].disassembly, x); break;      // Fx0A
                case 0x15: printf(instructions[28].disassembly, x); break;      // Fx15
                case 0x18: printf(instructions[29].disassembly, x); break;      // Fx18
                case 0x1e: printf(instructions[30].disassembly, x); break;      // Fx1E
                case 0x29: printf(instructions[31].disassembly, x); break;      // Fx29
                case 0x33: printf(instructions[32].disassembly, x); break;      // Fx33
                case 0x55: printf(instructions[33].disassembly, x); break;      // Fx55
                case 0x65: printf(instructions[34].disassembly, x); break;      // Fx65
                default: error_unrecognized_opcode(opcode);
            }
            break;
        default: error_unrecognized_opcode(opcode);
    }
}

void disassemble_program(StateDisassembler* state, uint8_t* codemap, uint8_t* labels) {
    // Print some metadata
    printf(";------------------------\n");
    printf("; ROM Size: %u Bytes\n", state->buffer_size - 0x200);
    printf(";------------------------");

    state->pc = PC_START;

    /* ------------- If pre-process was skipped, then do a lazy disassemble and exit. */
    if (codemap == NULL || labels == NULL) {
        while(state->pc < state->buffer_size) {
            disassemble_instruction(state, NULL, 1);
            state->pc += PC_STEP_SIZE;
        }

        return;
    }

    // Vars for formatting and decoding data bytes
    unsigned int db_flag = 0;
    unsigned int db_line_count = 0;
    char* decoded_buffer = calloc(state->buffer_size, 1);
    unsigned int decoded_count = 0;

    /* ------------- Do the full disassembly. ------------- */
    while(state->pc < state->buffer_size) {
        // If we find the address in the labels map
        if (labels[state->pc]) {
            // Check if done decoding text and print it out
            if (db_flag) {
                print_decoded_text(decoded_buffer, &decoded_count);
            }

            // Create/print a unique label
            printf("\nL%03x:", state->pc);
            
            db_flag = 0;
            db_line_count = 0;
        }

        // If the address is in the codemap
        if (codemap[state->pc]) {
            // Check if done decoding text and print it out
            if (db_flag) {
                print_decoded_text(decoded_buffer, &decoded_count);
            }

            // Disassemble/print the instructions
            printf("\n    ");
            disassemble_instruction(state, labels, 0);

            db_flag = 0;
            db_line_count = 0;
            state->pc += PC_STEP_SIZE;
        }
        // Address is neither a label nor code
        else {
            // Print binary contents as a hex value and increments pc
            if (db_flag) {
                if (db_line_count < 4) {
                    printf(", #%02x", state->buffer[state->pc]);
                }
                else {
                    printf("\n          #%02x", state->buffer[state->pc]);
                    db_line_count = 0;
                }
            }
            else {
                printf("\n    DB    #%02x", state->buffer[state->pc]);

                db_flag = 1;
                db_line_count = 0;
                decoded_count = 0;
            }

            // Building string of decoded text
            char decoded_char = (char)state->buffer[state->pc];
            if (decoded_char == '\n') {
                decoded_char = ' ';
            }
            decoded_buffer[decoded_count] = decoded_char;
            decoded_count++;
            
            // Incrementing past this data byte
            db_line_count++;
            (state->pc)++;
        }
    }
}

int main(int argc, char** argv) {
    // Check for file
    if (argc <= 1) {
        printf("Error: No binary ROM provided.");
        exit(1);
    }  
    
    /* ------ Initialize state and file buffer ------ */
    StateDisassembler* state = state_disassembler_new();
    load_file_into_state_disassembler(state, argv[1]);

    /* ------ Preprocess the program (looking for labels and code) ------ */
    Queue* segments = queue_new();
    uint8_t* codemap = calloc(state->buffer_size, 1);
    uint8_t* labels = calloc(state->buffer_size, 1);
    pre_process_program(state, segments, codemap, labels);

    /* ------ Disassemble the program ------ */
    disassemble_program(state, codemap, labels);

    // Clean up
    state_disassembler_delete(&state);
    queue_delete(&segments);
    free(codemap);
    free(labels);

    return 0;
}