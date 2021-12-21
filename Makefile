# Compiler to be used
CC = gcc

SRC = src
BIN = bin

DASM_H_FILES = c8dasm.h chip8.h instructions.h
DASM_C_FILES = c8dasm.c
DASM_O_FILES = c8dasm.o

DASM_H_PATHS = $(addprefix $(SRC)\\,$(DASM_H_FILES))
DASM_C_PATHS = $(addprefix $(SRC)\\,$(DASM_C_FILES))
DASM_O_PATHS = $(addprefix $(SRC)\\,$(DASM_O_FILES))

EMU_H_FILES = chip8.h
EMU_C_FILES = emulator.c
EMU_O_FILES = emulator.o

EMU_H_PATHS = $(addprefix $(SRC)\\,$(EMU_H_FILES))
EMU_C_PATHS = $(addprefix $(SRC)\\,$(EMU_C_FILES))
EMU_O_PATHS = $(addprefix $(SRC)\\,$(EMU_O_FILES))

DASM_OUT = $(BIN)\dasm.exe
EMU_OUT = $(BIN)\emulator.exe

# ---------------------------------------------------

dasm: $(DASM_OUT)

$(DASM_OUT): $(DASM_O_PATHS) $(DASM_H_PATHS)
	$(CC) -o $@ $^

emu: $(EMU_OUT)

$(EMU_OUT): $(EMU_O_PATHS)
	$(CC) -o $@ $^

clean:
	del $(EMU_O_PATHS)
	del $(DASM_O_PATHS)
	del $(EMU_OUT)
	del $(DASM_OUT)