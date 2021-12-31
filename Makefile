# Compiler to be used
CC=gcc
CFLAGS=

# Directories
SRC=src
BUILD=build
BIN=bin

# Object files
DASM_OBJS=disassembler.o chip8.o instructions.o queue.o 
EMU_OBJS=emulator.o chip8.o instructions.o

# Full object paths
DASM_OBJ_PATHS=$(addprefix $(BUILD)/,$(DASM_OBJS))
EMU_OBJ_PATHS=$(addprefix $(BUILD)/,$(EMU_OBJS))

# Targets
DASM_TARGET=$(BIN)/disassembler.exe
EMU_TARGET=$(BIN)/emulator.exe

# --------------------------------------------------

all: emulator disassembler

emulator: $(EMU_TARGET)

$(EMU_TARGET): $(EMU_OBJ_PATHS)
	$(CC) $(CFLAGS) $^ -o $@

disassembler: $(DASM_TARGET)

$(DASM_TARGET): $(DASM_OBJ_PATHS)
	$(CC) $(CFLAGS) $^ -o $@

$(BUILD)/%.o: $(SRC)/%.c
	$(CC) $(CFLAGS) -c $^ -o $@

clean:
	rm $(BUILD)/*.o $(BIN)/*