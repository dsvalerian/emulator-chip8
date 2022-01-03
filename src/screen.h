#ifndef SCREEN
#define SCREEN

#define SCREEN_W 1280
#define SCREEN_H 640

#include <SDL2/SDL.h>

typedef struct Screen {
    SDL_Renderer* renderer;
    SDL_Window* window;
} Screen;

/**
 * @brief Creates a new Screen.
 * 
 * @return Screen*
 */
Screen* screen_new();

/**
 * @brief Destroys/deletes a Screen.
 * 
 * @param screen Double pointer to Screen to be destroyed.
 */
void screen_delete(Screen** screen);

void prepare_scene(Screen* screen);

void present_scene(Screen* screen);

#endif