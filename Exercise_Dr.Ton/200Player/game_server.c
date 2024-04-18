
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include <semaphore.h> 
#include <unistd.h>

#define BUFFER_SIZE 100
int game[BUFFER_SIZE];
int count=0;

sem_t login_lock;


void logout()
{
    count--;
    game[count]=0;

}

void play()
{
    int playing_time = (rand() % 3)*1 + 1;
    sleep(playing_time);
    logout();

}

void retry()
{
    sleep(5);
}

int array[200];

void login(int v)
{   
    if (count==BUFFER_SIZE)
    {
        
        retry();
    }
    else
    {
        sem_post(&login_lock);
        printf("Player #%d log in.\n",v);
        array[v]=1;
        game[count]=1;
        count++;
        play();
        sem_wait(&login_lock);
        
    }
        
}

void *player(int v)
{
    time_t now = time(NULL);
    while (time(NULL)<= now + 60)
    {
        login(v);
    }
    
}



int main(int argc, char const *argv[])
{
    srand(time(NULL));
    sem_init(&login_lock,0,0);
    pthread_t player_pid[200];
    for (int i = 0; i < 200; i++)
    {
        printf("Player #%d is on.\n",i);
        pthread_create(&player_pid[i],NULL,player(i),NULL);
        pthread_join(player_pid[i],NULL);
    }

    sem_destroy(&login_lock);
    return 0;
}
