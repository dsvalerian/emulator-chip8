#ifndef INSTRUCTIONS
#define INSTRUCTIONS

#include <stdio.h>

/**
 * @brief Holds data about a CHIP-8 instruction.
 * 
 */
typedef struct Instruction {
    char* opcode;
    char* disassembly;
    void* func;
} Instruction;

/**
 * @brief All of handled CHIP-8 instructions.
 * 
 */
static Instruction instructions[35] = {
    {"0nnn", "SYS   $%03x", NULL},              // SYS  addr
    {"00E0", "CLS", NULL},                      // CLS
    {"00EE", "RET", NULL},                      // RET
    {"1nnn", "JP    $%03x", NULL},              // JP   addr
    {"2nnn", "CALL  $%03x", NULL},              // CALL addr
    {"3xkk", "SE    V%u, #%02x", NULL},         // SE   Vx, byte
    {"4xkk", "SNE   V%u, #%02x", NULL},         // SNE  Vx, byte
    {"5xy0", "SE    V%u, V%u", NULL},           // SE   Vx, Vy
    {"6xkk", "LD    V%u, #%02x", NULL},         // LD   Vx, byte
    {"7xkk", "ADD   V%u, #%02x", NULL},         // ADD  Vx, byte
    {"8xy0", "LD    V%u, V%u", NULL},           // LD   Vx, Vy
    {"8xy1", "OR    V%u, V%u", NULL},           // OR   Vx, Vy
    {"8xy2", "AND   V%u, V%u", NULL},           // AND  Vx, Vy
    {"8xy3", "XOR   V%u, V%u", NULL},           // XOR  Vx, Vy
    {"8xy4", "ADD   V%u, V%u", NULL},           // ADD  Vx, Vy
    {"8xy5", "SUB   V%u, V%u", NULL},           // SUB  Vx, Vy
    {"8xy6", "SHR   V%u", NULL},                // SHR  Vx
    {"8xy7", "SUBN  V%u, V%u", NULL},           // SUBN Vx, Vy
    {"8xyE", "SHL   V%u", NULL},                // SHL  Vx
    {"9xy0", "SNE   V%u, V%u", NULL},           // SNE  Vx, Vy
    {"Annn", "LD    I, $%03x", NULL},           // LD   I, addr
    {"Bnnn", "JP    V0, $%03x", NULL},          // JP   V0, addr
    {"Cxkk", "RND   V%u, #%02x", NULL},         // RND  Vx, byte
    {"Dxyn", "DRW   V%u, V%u, #%02x", NULL},    // DRW  Vx, Vy, nibble
    {"Ex9E", "SKP   V%u", NULL},                // SKP  Vx
    {"ExA1", "SKNP  V%u", NULL},                // SKNP Vx
    {"Fx07", "LD    V%u, DT", NULL},            // LD   Vx, DT
    {"Fx0A", "LD    V%u, K", NULL},             // LD   Vx, K
    {"Fx15", "LD    DT, V%u", NULL},            // LD   DT, Vx
    {"Fx18", "LD    ST, V%u", NULL},            // LD   ST, Vx
    {"Fx1E", "ADD   I, V%u", NULL},             // ADD  I, Vx
    {"Fx29", "LD    F, V%u", NULL},             // LD   F, Vx
    {"Fx33", "LD    B, V%u", NULL},             // LD   B, Vx
    {"Fx55", "LD    (I), V%u", NULL},           // LD   (I), Vx
    {"Fx65", "LD    V%u, (I)", NULL}            // LD   Vx, (I)
};

#endif