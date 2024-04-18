
#include <stdio.h>
#include <string.h>

#define NB_DISK_PAGE 5
#define PAGE_SIZE 20
#define NB_MEM_PAGE 4

#define boolean int
#define true 1
#define false 0

char Disk[NB_DISK_PAGE][PAGE_SIZE];
char Memory[NB_MEM_PAGE][PAGE_SIZE];
int PageTable[NB_DISK_PAGE];
struct {
	boolean free;
	int date;
	int npage;
} MemState[NB_MEM_PAGE];

int Date = 1;

void init();
char *memory_read(int npage);
int memory_alloc();
void page_fault(int npage);
int lru_select();

void init() {
	int i;

	// initialize Disk
	for (i=0;i<NB_DISK_PAGE;i++) {
		strcpy(Disk[i],"page");
		Disk[i][4] = '0'+i;
		Disk[i][5] = 0;
	}
	/*
	for (i=0;i<NB_DISK_PAGE;i++) {
		printf(Disk[i]);
		printf("\n");
	}
	*/

	// initialize Memory
	for (i=0;i<NB_MEM_PAGE;i++)
		MemState[i].free = true;

	// initialize PageTable
	for (i=0;i<NB_DISK_PAGE;i++)
		PageTable[i] = -1;
}


char *memory_read(int npage) {
printf("memory_read (%d)\n",npage);

 if (PageTable[npage]==-1)
 {
 page_fault(npage);
 }
MemState[PageTable[npage]].date=Date++;	
 return Memory[PageTable[npage]];
}

int memory_alloc() {
printf("memory_alloc: %d\n",0);
for (int i=0;i<NB_MEM_PAGE;i++)
{
	if (MemState[i].free==true)
	{
	MemState[i].free=false;
	return i;
	}
}
return -1;
// to be completed
}

void page_fault(int npage) {
printf("page_fault (%d)\n",npage);
int c = memory_alloc();
if (c==-1)
{
	c=lru_select();
	bcopy(Memory[c],Disk[MemState[c].npage],PAGE_SIZE);
	PageTable[MemState[c].npage]=-1;
	//MemState[c].npage=npage;
	//MemState[c].date=Date;
	//PageTable[npage]=c;
	//Memory[npage]=Disk[npage];
}
bcopy(Disk[npage],Memory[c],PAGE_SIZE);
PageTable[npage]=c;
MemState[c].free=false;
MemState[c].npage=npage;
printf("(%d) to Mem[%d]\n",npage,c);

}


int lru_select() {


int lowestdate=100000000;
int lowestpage=0;
for (int i=0;i<NB_MEM_PAGE;i++)
{
	if (MemState[i].date<lowestdate)
	{
	lowestdate=MemState[i].date;
	lowestpage=i;
	}
}
printf("lru_select: %d\n",lowestpage);
return lowestpage;
}

int main(int argc, char *argv[]) {
	int i;
	printf("initialization\n");
	init();

/*
	printf("linear access of all pages\n");
	for (i=0;i<NB_DISK_PAGE;i++)
		printf("read access page %d : %s\n",i,memory_read(i));
*/

	printf("access pages as in lecture (0,1,2,3,0,1,4,0,1,2,3,4,1,2)\n");
	int serie[14] = {0,1,2,3,0,1,4,0,1,2,3,4,1,2};
	for (i=0;i<14;i++)
	{
		printf("read access page %d : %s\n",serie[i],memory_read(serie[i]));
	
	}
	printf("completed\n");
	printf("print memory_state\n");
	for (i=0;i<NB_MEM_PAGE;i++)
		printf("%d ",MemState[i].npage);
	printf("\n");
	printf("completed\n");
}



