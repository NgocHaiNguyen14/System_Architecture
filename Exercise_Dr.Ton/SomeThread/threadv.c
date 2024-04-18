#include <stdio.h>
#include <pthread.h>

int n = 1;
void *thread1(void* arg)
{
    int stat = 1;
   for (int i=0;i<10;i++)
   {
        printf("\nThread 1 Loop %d n = %d: %d * %d = %d",i,n,n,stat,n*stat);
        n++;
   }

}
void *thread2(void* arg)
{
    int stat = 2;
   for (int i=0;i<10;i++)
   {
        printf("\nThread 2 Loop %d n = %d: %d * %d = %d",i,n,n,stat,n*stat);
        n++;
   }
    
}
void *thread3(void* arg)
{
    int stat = 3;
   for (int i=0;i<10;i++)
   {
        printf("\nThread 3 Loop %d n = %d: %d * %d = %d",i,n,n,stat,n*stat);
        n++;
   }
}

int main(int arg, char** argv)
{
    
    pthread_t id;
    pthread_create(&id,NULL,&thread1,NULL);
    pthread_create(&id,NULL,&thread2,NULL);
    pthread_create(&id,NULL,&thread3,NULL);
    pthread_create(&id,NULL,&thread1,NULL);
    pthread_create(&id,NULL,&thread2,NULL);
    pthread_create(&id,NULL,&thread3,NULL);
    pthread_create(&id,NULL,&thread1,NULL);
    pthread_create(&id,NULL,&thread2,NULL);
    pthread_create(&id,NULL,&thread3,NULL);
    pthread_exit(NULL);
    return 0;
    

}
/* The code above will create 9 different thread in the order of  
1 - 2 - 3 - 1 - 2 - 3 - 1 - 2 - 3. Each thread printf their name 
and do a small computation with the global n.
By executing the code, we can see each thread run independantly 
and not wait for the before thread to execute.  */