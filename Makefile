# Compiler to be used
CC=gcc
CFLAGS=-Wall

# Directories
SRC=src
BIN=bin

# Object files
DASM_OBJS=disassembler.o queue.o

# Full object paths
DASM_OBJ_PATHS=$(addprefix $(SRC)/,$(DASM_OBJS))

# Targets
DASM_TARGET=$(BIN)/disassembler.exe

# --------------------------------------------------

dasm: $(DASM_TARGET)

$(DASM_TARGET): $(DASM_OBJ_PATHS)
	$(CC) $(CFLAGS) $^ -o $@

$(SRC)/%.o: $(SRC)/%.c
	$(CC) $(CFLAGS) -c $^ -o $@

clean:
	rm $(SRC)/*.o $(BIN)/*