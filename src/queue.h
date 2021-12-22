#ifndef QUEUE
#define QUEUE

#include<stdint.h>

typedef struct Node {
    uint16_t value;
    struct Node* next;
} Node;

typedef struct Queue {
    Node* head;
} Queue;

/**
 * @brief Allocates and initializes a Queue.
 * 
 * @return Queue* Pointer to a Queue struct.
 */
Queue* queue_new();

/**
 * @brief Pushes an element to the queue.
 * 
 * @param queue The queue to push to.
 * @param value The value to push.
 */
void queue_push(Queue* queue, uint16_t value);

/**
 * @brief Pops an element off the queue.
 * 
 * @param queue The queue to pop from.
 * @return uint16_t The popped value.
 */
uint16_t queue_pop(Queue* queue);

/**
 * @brief Checks if a queue contains a value. O(n) complexity.
 * 
 * @param queue The queue to check.
 * @param value The value to check for.
 * @return int 1 if the queue contains the value, 0 otherwise.
 */
int queue_contains(Queue* queue, uint16_t value);

/**
 * @brief Deletes a queue by freeing all of its allocated memory and setting it's pointer to NULL.
 * 
 * @param queue_ptr Double pointer to the queue being deleted.
 */
void queue_delete(Queue** queue_ptr);

#endif