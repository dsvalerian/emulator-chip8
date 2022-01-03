cmake_minimum_required(VERSION 3.10)
project(emulator-chip8 VERSION 0.1)

# Dependencies
find_package(SDL2 REQUIRED)
include_directories(${SDL2_INCLUDE_DIRS})

add_executable(emulator emulator.c chip8.c instructions.c screen.c)
add_executable(disassembler disassembler.c chip8.c instructions.c queue.c)

target_include_directories(emulator PUBLIC ${SDL2_INCLUDE_DIRS})
target_link_libraries(emulator PUBLIC ${SDL2_LIBRARIES} ${SDL2_DEPENDENCIES})