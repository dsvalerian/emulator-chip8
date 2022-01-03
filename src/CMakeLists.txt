cmake_minimum_required(VERSION 3.10)
project(emulator-chip8 VERSION 0.1)

add_executable(emulator emulator.c chip8.c instructions.c)
add_executable(disassembler disassembler.c chip8.c instructions.c queue.c)