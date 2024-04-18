#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include <semaphore.h> 
#include <unistd.h>

#define BUFFER_SIZE 100
int game_slot[BUFFER_SIZE];
int can_login[BUFFER_SIZE];

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
int count=0;
sem_t login_lock;

void play()
{
    int playing_time = (rand() % 20) + 10;
    sleep(playing_time);

}

void login(int v)
{
    if (count == BUFFER_SIZE)
    {
        printf("#%d login failed, Lobby is full.\n",v);

        
    }
    else
    {
        sem_wait(&login_lock);
        printf("#%d log in.\n",v);
        pthread_mutex_lock(&mutex);
        game_slot[count]=1;
        count++;
        pthread_mutex_unlock(&mutex);
        play();
        printf("#%d log out.\n",v);
        pthread_mutex_lock(&mutex);
        count--;
        game_slot[count]=0;
        pthread_mutex_unlock(&mutex);
        sem_post(&login_lock);
    }
    
    
}

void *player(void* arg)
{
    int v = *(int*) arg;
    freopen("gamelog.txt", "w+", stdout); 
    //printf("#%d created.\n",v);
    time_t start = time(NULL);
    while (start *60*5 >= time(NULL))
    {
        login(v);
        sleep(5);
    }
    

    pthread_exit(NULL);
}

int main(int argc, char const *argv[])
{
    int n = 200;
    pthread_t player_id[n];
    int num[n];
    for (int i = 0; i < BUFFER_SIZE; i++)
    {
        game_slot[i]=0;
        can_login[i]=0;
    }
    
    sem_init(&login_lock,0,BUFFER_SIZE);
    for (int i = 0; i < n; i++)
    {
        num[i]= i;
        pthread_create(&player_id[i],NULL,player, (void * ) &num[i]);
    }
    

    for (int i = 0; i < n; i++)
    {
        pthread_join(player_id[i],NULL);
    }
    int sum =0;
    for (int i = 0; i < BUFFER_SIZE; i++)
    {
        sum += can_login[i];
    }
    printf("%d of people got in",sum);
    return 0;
}
