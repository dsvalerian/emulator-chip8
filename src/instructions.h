#ifndef INSTRUCTIONS
#define INSTRUCTIONS

#include <stdio.h>

#include "chip8.h"

void ret(StateChip8* state);
void jp(StateChip8* state, uint16_t addr);
void call(StateChip8* state, uint16_t addr);
void se(StateChip8* state, uint8_t x, uint8_t byte);
void ld_vx_byte(StateChip8* state, uint8_t x, uint8_t byte);
void add(StateChip8* state, uint8_t x, uint8_t byte);
void ld_vx_vy(StateChip8* state, uint8_t x, uint8_t y);
void ld_i_addr(StateChip8* state, uint16_t addr);
void drw(StateChip8* state, uint8_t x, uint8_t y, uint8_t n);

void error_unhandled_instruction(StateChip8* state);

/**
 * @brief Holds data about a CHIP-8 instruction.
 * 
 */
typedef struct Instruction {
    char* opcode;
    char* disassembly;
    char* lazy_disassembly;
    void* func;
} Instruction;

/**
 * @brief All of handled CHIP-8 instructions.
 * 
 */
static Instruction instructions[35] = {
    {"0nnn", "SYS   $%03x", NULL, error_unhandled_instruction},             // SYS  addr
    {"00E0", "CLS", NULL, error_unhandled_instruction},                     // CLS
    {"00EE", "RET", NULL, ret},                                             // RET
    {"1nnn", "JP    L%03x", "JP    $%03x", jp},                             // JP   addr
    {"2nnn", "CALL  L%03x", "CALL  $%03x", call},                           // CALL addr
    {"3xkk", "SE    V%u, #%02x", NULL, se},                                 // SE   Vx, byte
    {"4xkk", "SNE   V%u, #%02x", NULL, error_unhandled_instruction},        // SNE  Vx, byte
    {"5xy0", "SE    V%u, V%u", NULL, error_unhandled_instruction},          // SE   Vx, Vy
    {"6xkk", "LD    V%u, #%02x", NULL, ld_vx_byte},                         // LD   Vx, byte
    {"7xkk", "ADD   V%u, #%02x", NULL, add},                                // ADD  Vx, byte
    {"8xy0", "LD    V%u, V%u", NULL, ld_vx_vy},                             // LD   Vx, Vy
    {"8xy1", "OR    V%u, V%u", NULL, error_unhandled_instruction},          // OR   Vx, Vy
    {"8xy2", "AND   V%u, V%u", NULL, error_unhandled_instruction},          // AND  Vx, Vy
    {"8xy3", "XOR   V%u, V%u", NULL, error_unhandled_instruction},          // XOR  Vx, Vy
    {"8xy4", "ADD   V%u, V%u", NULL, error_unhandled_instruction},          // ADD  Vx, Vy
    {"8xy5", "SUB   V%u, V%u", NULL, error_unhandled_instruction},          // SUB  Vx, Vy
    {"8xy6", "SHR   V%u", NULL, error_unhandled_instruction},               // SHR  Vx
    {"8xy7", "SUBN  V%u, V%u", NULL, error_unhandled_instruction},          // SUBN Vx, Vy
    {"8xyE", "SHL   V%u", NULL, error_unhandled_instruction},               // SHL  Vx
    {"9xy0", "SNE   V%u, V%u", NULL, error_unhandled_instruction},          // SNE  Vx, Vy
    {"Annn", "LD    I, $%03x", NULL, ld_i_addr},                            // LD   I, addr
    {"Bnnn", "JP    V0, $%03x", NULL, error_unhandled_instruction},         // JP   V0, addr
    {"Cxkk", "RND   V%u, #%02x", NULL, error_unhandled_instruction},        // RND  Vx, byte
    {"Dxyn", "DRW   V%u, V%u, #%02x", NULL, drw},                           // DRW  Vx, Vy, nibble
    {"Ex9E", "SKP   V%u", NULL, error_unhandled_instruction},               // SKP  Vx
    {"ExA1", "SKNP  V%u", NULL, error_unhandled_instruction},               // SKNP Vx
    {"Fx07", "LD    V%u, DT", NULL, error_unhandled_instruction},           // LD   Vx, DT
    {"Fx0A", "LD    V%u, K", NULL, error_unhandled_instruction},            // LD   Vx, K
    {"Fx15", "LD    DT, V%u", NULL, error_unhandled_instruction},           // LD   DT, Vx
    {"Fx18", "LD    ST, V%u", NULL, error_unhandled_instruction},           // LD   ST, Vx
    {"Fx1E", "ADD   I, V%u", NULL, error_unhandled_instruction},            // ADD  I, Vx
    {"Fx29", "LD    F, V%u", NULL, error_unhandled_instruction},            // LD   F, Vx
    {"Fx33", "LD    B, V%u", NULL, error_unhandled_instruction},            // LD   B, Vx
    {"Fx55", "LD    (I), V%u", NULL, error_unhandled_instruction},          // LD   (I), Vx
    {"Fx65", "LD    V%u, (I)", NULL, error_unhandled_instruction}           // LD   Vx, (I)
};

#endif