#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
int Acompute1()
{
    return 4;
}

int Acompute2(int v)
{
    printf("A process receive %d",v);
    return 0;
}

int Bcompute1()
{
    return 9;
}

int Bcompute2(int v)
{
    printf("B process receive %d",v);
    return 0;
}


int main() {
    int pAtoB[2];
    pipe(pAtoB);
    int pBtoA[2];
    pipe(pBtoA);

    pid_t processA = fork();
    if (processA == 0) {
        // Child process A
        printf("running A\n");
        int resA = Acompute1();
        int resB = 0;
        
        dup2(pAtoB[1], 1); 
        dup2(pBtoA[0], 0); 
        close(pAtoB[0]);
        close(pAtoB[1]);
        printf("%d", resA);
        
        fgets("%d", &resB,1);
        Acompute2(resB);
        close(pBtoA[0]);
        close(pBtoA[1]); 
    } else {
        pid_t processB = fork();
        if (processB == 0) {
            // Child process B
            printf("running B\n");
            int resB = Bcompute1();
            int resA = 0;
            dup2(pBtoA[1], 1); 
            dup2(pAtoB[0], 0); 
            close(pBtoA[0]);
            close(pBtoA[1]);
            printf("%d", resB);
            fgets("%d", &resA,1);
            Bcompute2(resA);
            close(pAtoB[0]);
            close(pAtoB[1]);
        } else {
            // Parent process

            wait(NULL); // Wait for both child processes to finish
            wait(NULL);

            
            close(pAtoB[0]);
            close(pAtoB[1]);
            close(pBtoA[0]);
            close(pBtoA[1]); // Close all pipe ends in the parent process
        }
    }
    return 0;
}
