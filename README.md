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
The CHIP-8 disassembler is a small utility meant to take a binary CHIP-8 ROM file as input and print out the corresponding CHIP-8 assembly language of the file. The disassembled code is very basic and is less human-readable than written assembly (no functions, comments, etc). Not all operations are implemented yet, so there will be blank lines in the output.

### Compiling
There is a Makefile included with the project.

To build the disassembler with `make`:
1. Navigate to the root directory of the project
2. Run `make dasm`

### Usage
The disassembler requires a CHIP-8 ROM file as input. Once you have obtained a ROM file and compiled the disassembler into an executable, simply run `<path_to_executable> <path_to_rom_file> > <path_to_output_file>`.

## Emulator
The CHIP-8 emulator is a utility meant to take a binary CHIP-8 ROM file as input and emulate the program as closely to the CHIP-8 as possible. At the moment, none of it is implemented.
