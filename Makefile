# Compiler to be used
CC = gcc

SRC = src
BIN = bin

DIS_H_FILES = disassembler.h chip8.h
DIS_C_FILES = disassembler.c
DIS_O_FILES = disassembler.o

DIS_H_PATHS = $(addprefix $(SRC)\\,$(DIS_H_FILES))
DIS_C_PATHS = $(addprefix $(SRC)\\,$(DIS_C_FILES))
DIS_O_PATHS = $(addprefix $(SRC)\\,$(DIS_O_FILES))

EMU_H_FILES = chip8.h
EMU_C_FILES = emulator.c
EMU_O_FILES = emulator.o

EMU_H_PATHS = $(addprefix $(SRC)\\,$(EMU_H_FILES))
EMU_C_PATHS = $(addprefix $(SRC)\\,$(EMU_C_FILES))
EMU_O_PATHS = $(addprefix $(SRC)\\,$(EMU_O_FILES))

DIS_OUT = $(BIN)\disassembler.exe
EMU_OUT = $(BIN)\emulator.exe

# ---------------------------------------------------

dis: $(DIS_OUT)

$(DIS_OUT): $(DIS_O_PATHS) $(DIS_H_PATHS)
	$(CC) -o $@ $^

emu: $(EMU_OUT)

$(EMU_OUT): $(EMU_O_PATHS)
	$(CC) -o $@ $^

#$(EMU_O_PATHS): $(EMU_C_PATHS) $(EMU_H_PATHS)#
#	$(CC) -o $@ $^

clean:
	del $(EMU_O_PATHS)
	del $(DIS_O_PATHS)
	del $(EMU_OUT)
	del $(DIS_OUT)