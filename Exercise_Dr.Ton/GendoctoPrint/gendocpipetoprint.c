#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>

#define STDIN = 0
#define STDOUT = 1

int main()
{
    int p[2];
    pipe(p);
    {
        int f = fork();
        if (f!=0)
        {
            printf("Running gendoc://\n");
            dup2(p[1],1);

            
            if(execlp("./gendoc","gendoc",NULL)!=0)
	        {printf("Gendoc not found\n");}
            close(p[1]);
            close(p[0]);
        }
        else
        {
            printf("Running print://\n");
            dup2(p[0],0);
            if(execlp("./print","print",NULL)!=0)
	        {printf("Print not found\n");}
            close(p[1]);
            close(p[0]); 
        }
    }
    return 0;
}