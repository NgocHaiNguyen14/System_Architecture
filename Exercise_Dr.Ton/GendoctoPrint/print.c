#include <stdio.h>

int main() {
    char buffer[1024];
    printf("Printing document received from stdin:\n");
    while (fgets(buffer, sizeof(buffer), stdin) != NULL) {
        printf("%s", buffer);
    }
    return 0;
}