
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>


#define BUFFER_SIZE 10
int buffer[BUFFER_SIZE];
int buffer_verify[BUFFER_SIZE];

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex2 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t produce_done = PTHREAD_COND_INITIALIZER;
pthread_cond_t verify_done = PTHREAD_COND_INITIALIZER;
pthread_cond_t consume_done = PTHREAD_COND_INITIALIZER;


pthread_cond_t sleepcond = PTHREAD_COND_INITIALIZER;
pthread_mutex_t sleepmutex = PTHREAD_MUTEX_INITIALIZER;

int count = 0,count_v = 0, need_verify = 0;

void display_array(int v)
{
    for (size_t i = 0; i < v; i++)
    {
        printf("|x|");
    }
    for (size_t i = 0; i < BUFFER_SIZE-v; i++)
    {
        printf("| |");
    }
    
    
}

void display() {
    printf("\n----------------------\n");
    printf("Buffer count : %d \n",count);
    display_array(count);
    printf("\nBuffer verify count : %d \n",count_v);
    display_array(count_v);   
    printf("\n----------------------\n");
}

void produce(){
    pthread_mutex_lock(&mutex);
    if (count==BUFFER_SIZE)
    {
        pthread_cond_wait(&consume_done,&mutex);
    }
    printf("producing\n");
    buffer[count] = 1;
    count++;

    pthread_cond_signal(&produce_done);
    display();
	pthread_mutex_unlock(&mutex);
}

void verify(){
    pthread_mutex_lock(&mutex);
	pthread_mutex_unlock(&mutex2);

    if (count == 0)
    {
    pthread_cond_wait(&produce_done,&mutex);
    }
    printf("verify\n");
    int ava = rand() % 5;
    if (ava == 0)
    {
        printf("verify failed...\n");
        count--;
        buffer[count]=0;
        
        
    } 
    else 
    {
        printf("verify success..\n");
        count--;
        buffer[count]=0;
        if (count==0)
        buffer_verify[count_v]=1;
        count_v++;
        pthread_cond_signal(&verify_done);

    }
    
    display();
	pthread_mutex_unlock(&mutex);
    
	pthread_mutex_unlock(&mutex2);
}

void consume(){
    pthread_mutex_lock(&mutex2);
    if (count_v==0)
    {
        pthread_cond_wait(&verify_done,&mutex);
    }
    printf("consume\n");
    buffer_verify[count_v] = 0;
    count_v--;
    pthread_cond_signal(&consume_done);
    display();
	pthread_mutex_unlock(&mutex2);
}

void sleep() {
	struct timespec t;
	t.tv_sec = time(0) + (rand() % 2) + 1;
	t.tv_nsec = 0;
	pthread_mutex_lock(&sleepmutex);
	pthread_cond_timedwait(&sleepcond, &sleepmutex, &t);
	pthread_mutex_unlock(&sleepmutex);
}
         

	
void *consumer(void *threadid)
{
   for(;;) {
	consume();
	sleep();
   }
}

void *producer(void *threadid)
{
   for(;;) {
	produce();
    
	sleep();
   }
}


void *verifier(void *threadid)
{
   for(;;) {
	verify();
	sleep();
   }
}








int main(int argc, char const *argv[])
{
    freopen("output.txt", "a+", stdout); 
    pthread_t producer_pid, consumer_pid, verifier_pid;
	pthread_create(&producer_pid, NULL, producer,NULL);
    pthread_create(&verifier_pid, NULL, verifier,NULL);
 	pthread_create(&consumer_pid, NULL, consumer,NULL);
 	pthread_join( producer_pid, NULL);
    pthread_join( verifier_pid, NULL);
    pthread_join( consumer_pid, NULL);
    return 0;
}
