# emulator-chip8
A basic CHIP-8 disassembler and emulator written in C.

### Sections:
- [Overview](#overview)
- [Disassembler](#disassembler)
  -  [Usage](#usage)
- [Emulator](#overview)
- [Compiling](#compiling)

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

### Usage
The disassembler requires a CHIP-8 ROM file as input. Once you have obtained a ROM file and compiled the disassembler into an executable, simply run `<path_to_disassembler> <path_to_rom_file>` in the root directory.

Below is a small section of output disassembly of a free public domain CHIP-8 game Flight Runner, which can be found [here](https://johnearnest.github.io/chip8Archive/).

```
$ ./bin/disassembler.exe rom/flightrunner.ch8
;------------------------
; ROM Size: 295 Bytes
;------------------------
    JP    L319
    DB    #f0, #f0, #18, #f0
          #ff, #44, #aa, #aa
          #aa, #44, #cd, #a9
          #c5, #84, #8d
; Decoded Text: ≡≡↑≡ D¬¬¬D═⌐┼äì
L211:
    LD    V12, #40
    LD    V11, #03
    LD    V10, #1c
    LD    V9, V11
    LD    V8, V10
    LD    V14, #04
    LD    V13, #0e
    LD    V0, #00
    LD    V1, V11
    LD    V2, V10
    LD    I, $206
L227:
    DRW   V0, V1, #01
    DRW   V0, V2, #01
    ADD   V0, #08
    SE    V0, #40
    JP    L227
    LD    I, $203
    DRW   V14, V13, #03
    RET
...
```

## Emulator
The CHIP-8 emulator is a utility meant to take a binary CHIP-8 ROM file as input and emulate the program as closely to the CHIP-8 as possible. At the moment, only some of the CPU state and certain instructions are implemented.

## Compiling
This project uses CMake to automate the build. Depending on your system, this will generate a Makefile, build.ninja file, Visual Studio solution, etc. You can then use that to build the targets (either `emulator` or `disassembler`). 

In my case, I use the MSYS2 environment on Windows, so my steps are as follows:
1. Navigate to the root project directory
2. Run `cmake -S src -B build -G "MSYS Makefiles"`
3. Run `make -C build/ <target_name>`
