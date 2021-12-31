# emulator-chip8
A basic CHIP-8 disassembler and emulator written in C.

### Sections:
- [Overview](#overview)
- [Disassembler](#disassembler)
  -  [Compiling](#compiling)
  -  [Usage](#usage)
- [Emulator](#overview)

## Overview
CHIP-8 is an interpreted programming language from the 1970s, initially used on the COSMAC VIP and Telmac 1800 8-bit microcomputers. CHIP-8 programs are run on a virtual machine.

Some notable CHIP-8 specifications:
- 4096 8-bit memory locations (4KB RAM)
- 12-bit address register
- 16 8-bit data registers named V0 to VF
- 35 2-byte opcodes, stored big-endian
- 64x32 display resolution
- 60hz delay and sound timers
- 16 input keys

## Disassembler
The CHIP-8 disassembler is a small utility meant to take a binary CHIP-8 ROM file as input and print out the corresponding CHIP-8 assembly language of the file. 

### Compiling
There is a Makefile included with the project. Simply run `make disassembler` in the root directory to compile the disassembler.

### Usage
The disassembler requires a CHIP-8 ROM file as input. Once you have obtained a ROM file and compiled the disassembler into an executable, simply run `./bin/disassembler.exe <path_to_rom_file>` in the root directory.

## Emulator
The CHIP-8 emulator is a utility meant to take a binary CHIP-8 ROM file as input and emulate the program as closely to the CHIP-8 as possible. At the moment, none of it is implemented.
