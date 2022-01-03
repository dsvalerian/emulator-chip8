#include "screen.h"

Screen* screen_new() {
    int window_flags = 0;
    int renderer_flags = SDL_RENDERER_ACCELERATED;

    // Initialize SDL
    if (SDL_Init(SDL_INIT_VIDEO) < 0) {
        printf("Failed to initialize SDL: %s\n", SDL_GetError());
        exit(1);
    }

    // Initialize screen window and renderer
    Screen* screen = malloc(sizeof(Screen));

    screen->window = SDL_CreateWindow("CHIP-8", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED,
        SCREEN_W, SCREEN_H, window_flags);
    if (!screen->window) {
        printf("Failed to open window: %s\n", SDL_GetError());
        exit(1);
    }

    SDL_SetHint(SDL_HINT_RENDER_SCALE_QUALITY, "linear");

    screen->renderer = SDL_CreateRenderer(screen->window, -1, renderer_flags);
    if (!screen->renderer) {
        printf("Failed to create renderer: %s\n", SDL_GetError());
        exit(1);
    }

    return screen;
}

void screen_delete(Screen** screen) {
    SDL_DestroyWindow((*screen)->window);
    SDL_DestroyRenderer((*screen)->renderer);
    free(*screen);
    *screen = NULL;
}

void prepare_scene(Screen* screen) {
    SDL_SetRenderDrawColor(screen->renderer, 96, 128, 255, 255);
    SDL_RenderClear(screen->renderer);
}

void present_scene(Screen* screen) {
    SDL_RenderPresent(screen->renderer);
}